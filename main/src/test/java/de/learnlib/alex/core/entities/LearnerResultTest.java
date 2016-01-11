/*
 * Copyright 2016 TU Dortmund
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.learnlib.alex.core.entities;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.learnlib.alex.core.entities.learnlibproxies.AlphabetProxy;
import de.learnlib.alex.core.entities.learnlibproxies.eqproxies.CompleteExplorationEQOracleProxy;
import net.automatalib.automata.transout.impl.compact.CompactMealy;
import net.automatalib.words.Alphabet;
import net.automatalib.words.impl.SimpleAlphabet;
import org.junit.Test;

import java.time.ZonedDateTime;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

public class LearnerResultTest {

    private static final Long USER_ID = 3L;
    private static final Long PROJECT_ID = 3L;
    private static final Long ID = 3L;
    private static final Long STEP_NO = 3L;
    private static final ZonedDateTime TEST_DATE = ZonedDateTime.parse("1970-01-01T00:00:00.000+00:00");
    private static final long TEST_DURATION = 9001;
    private static final int EQS_USED = 123;
    private static final String EXPECTED_JSON = "{\"configuration\":{"
                                                    + "\"algorithm\":\"TTT\",\"browser\":\"htmlunitdriver\","
                                                    + "\"comment\":\"\",\"eqOracle\":"
                                                        + "{\"type\":\"random_word\",\"minLength\":1,\"maxLength\":1,"
                                                        + "\"maxNoOfTests\":1},"
                                                    + "\"maxAmountOfStepsToLearn\":0,\"resetSymbol\":null,"
                                                    + "\"symbols\":[]},"
                                                + "\"counterExample\":\"\",\"hypothesis\":{"
                                                    + "\"nodes\":[0,1],\"initNode\":0,\"edges\":["
                                                        + "{\"from\":0,\"input\":\"0\",\"to\":0,\"output\":\"OK\"},"
                                                        + "{\"from\":0,\"input\":\"1\",\"to\":1,\"output\":\"OK\"},"
                                                        + "{\"from\":1,\"input\":\"0\",\"to\":1,\"output\":\"OK\"},"
                                                        + "{\"from\":1,\"input\":\"1\",\"to\":0,\"output\":\"OK\"}"
                                                + "]},"
                                                + "\"project\":" + PROJECT_ID + ",\"sigma\":[\"0\",\"1\"],"
                                                + "\"statistics\":{"
                                                    + "\"duration\":" + TEST_DURATION + ",\"eqsUsed\":0,\"mqsUsed\":0,"
                                                    + "\"startDate\":\"1970-01-01T00:00:00.000+00:00\","
                                                    + "\"symbolsUsed\":0"
                                                + "},"
                                                + "\"stepNo\":" + STEP_NO + ",\"testNo\":" + ID + ",\"user\":"
                                                    + USER_ID + "}";

    @Test
    public void shouldCreateTheCorrectJSON() throws JsonProcessingException {
        Alphabet<String> sigma = new SimpleAlphabet<>();
        sigma.add("0");
        sigma.add("1");

        CompactMealy<String, String> hypothesis = new CompactMealy<>(sigma);
        int state0 = hypothesis.addInitialState();
        int state1 = hypothesis.addState();

        hypothesis.addTransition(state0, "0", state0, "OK");
        hypothesis.addTransition(state0, "1", state1, "OK");
        hypothesis.addTransition(state1, "1", state0, "OK");
        hypothesis.addTransition(state1, "0", state1, "OK");

        Project project = mock(Project.class);
        given(project.getId()).willReturn(PROJECT_ID);

        LearnerResult.Statistics statistics = new LearnerResult.Statistics();
        statistics.setStartDate(TEST_DATE);
        statistics.setDuration(TEST_DURATION);

        User user = new User(USER_ID);

        LearnerResult result = new LearnerResult();
        result.setProject(project);
        result.setTestNo(ID);
        result.setStepNo(STEP_NO);
        result.setStatistics(statistics);
        result.setSigma(AlphabetProxy.createFrom(sigma));
        result.createHypothesisFrom(hypothesis);
        result.setUser(user);

        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(result);

        assertEquals(EXPECTED_JSON, json);
    }

    @Test
    public void shouldReadAndParseJSONCorrectly() throws Exception {
        String json = "{\"configuration\":{"
                            + "\"algorithm\":\"DHC\", \"eqOracle\": {\"type\": \"complete\"},"
                            + "\"maxAmountOfStepsToLearn\":0,\"symbols\":[]},"
                        + "\"hypothesis\": {"
                            + "\"nodes\": [0, 1], \"edges\": ["
                                + "{\"from\": 0, \"input\": 1, \"to\": 0, \"output\": \"OK\"},"
                                + "{\"from\": 0, \"input\": 2, \"to\": 1, \"output\": \"OK\"}"
                        + "]},"
                        + "\"testNo\":" + ID + ",\"project\":" + PROJECT_ID + ","
                        + "\"sigma\":[\"0\",\"1\"],\"stepNo\":" + STEP_NO + ", \"statistics\": {"
                            + "\"eqsUsed\":" + EQS_USED + ", \"duration\": " + TEST_DURATION + ", \"mqsUsed\":0,"
                            + "\"startDate\": \"1970-01-01T00:00:00.000+00:00\",\"symbolsUsed\":0}"
                        + "}";

        ObjectMapper objectMapper = new ObjectMapper();
        LearnerResult resultFromJSON = objectMapper.readValue(json, LearnerResult.class);

//
//        LearnerResult resultFromJSON = new LearnerResult();
        LearnerResult.Statistics statistics = new LearnerResult.Statistics();
        statistics.setEqsUsed(EQS_USED);
        statistics.setStartDate(TEST_DATE);
        statistics.setDuration(TEST_DURATION);
//
        assertEquals(PROJECT_ID, resultFromJSON.getProjectId());
        assertEquals(ID, resultFromJSON.getTestNo());
        assertEquals(STEP_NO, resultFromJSON.getStepNo());
        assertEquals(statistics.getDuration(), resultFromJSON.getStatistics().getDuration());
        assertEquals(statistics.getStartTime(), resultFromJSON.getStatistics().getStartTime());
        assertEquals(statistics.getEqsUsed(), resultFromJSON.getStatistics().getEqsUsed());
        assertEquals(LearnAlgorithms.DHC, resultFromJSON.getConfiguration().getAlgorithm());
        assertTrue(resultFromJSON.getConfiguration().getEqOracle() instanceof CompleteExplorationEQOracleProxy);
        assertEquals(2, resultFromJSON.getSigma().size());
        assertEquals(2, resultFromJSON.getHypothesis().getNodes().size());
        assertEquals(2, resultFromJSON.getHypothesis().getEdges().size());
    }

}
