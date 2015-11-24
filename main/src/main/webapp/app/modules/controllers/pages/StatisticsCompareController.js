import {chartMode} from '../../constants';

/**
 * The controller for the learn statistics page.
 */
// @ngInject
class StatisticsCompareController {

    /**
     * Constructor
     * @param SessionService
     * @param LearnResultResource
     * @param LearnerResultChartService
     * @param ToastService
     * @param $stateParams
     * @param $state
     */
    constructor(SessionService, LearnResultResource, LearnerResultChartService, ToastService, $stateParams, $state) {
        this.LearnResultResource = LearnResultResource;
        this.LearnerResultChartService = LearnerResultChartService;
        this.ToastService = ToastService;
        this.$stateParams = $stateParams;
        this.$state = $state;

        // make sure there is at least one test number given in the URL
        if (!$stateParams.testNos || $stateParams.testNos === '') {
            this.ToastService.danger('You have to select at least one result');
            this.$state.go('statistics');
            return;
        }

        // make sure the mode if acceptable
        if ($stateParams.mode !== chartMode.CUMULATED
            && $stateParams.mode !== chartMode.COMPLETE) {

            this.ToastService.danger('The chart mode should be cumulated or complete');
            this.$state.go('statistics');
            return;
        }

        /**
         * The project that is in the session
         * @type {Project}
         */
        this.project = SessionService.project.get();

        /**
         * The list of test result nos that are used for the chart
         * @type{number[]}
         */
        this.testNos = $stateParams.testNos.split(',').map(t => Number.parseInt(t));

        /**
         * Make the chart mode dictionary available in the view
         * @type {object}
         */
        this.chartMode = chartMode;

        /**
         * The data to fill the charts
         * @type {object}
         */
        this.chartData = {};

        /**
         * The selected chart mode
         * @type {string}
         */
        this.selectedChartMode = $stateParams.mode;

        /**
         * If the charts should be shown in two columns
         * @type {boolean}
         */
        this.showInColumns = true;

        // depending on the mode, create a different chart
        if (this.selectedChartMode === chartMode.CUMULATED) {
            if (this.testNos.length === 1) {
                this.createChartSingleFinal(this.testNos[0]);
            } else {
                this.createChartMultipleFinal(this.testNos);
            }
        } else {
            if (this.testNos.length === 1) {
                this.createChartSingleComplete(this.testNos[0]);
            } else {
                this.createChartMultipleComplete(this.testNos);
            }
        }
    }

    /**
     * @param {number} testNo
     */
    createChartSingleFinal(testNo) {
        this.LearnResultResource.getFinal(this.project.id, testNo)
            .then(result => {
                this.chartData = this.LearnerResultChartService.createDataSingleFinal(result);
            })
    }

    /**
     * @param {number} testNo
     */
    createChartSingleComplete(testNo) {
        this.LearnResultResource.getComplete(this.project.id, testNo)
            .then(results => {
                this.chartData = this.LearnerResultChartService.createDataSingleComplete(results);
            })
    }

    /**
     * @param {number[]} testNos
     */
    createChartMultipleFinal(testNos) {
        this.LearnResultResource.getAllFinal(this.project.id).then(results => {

            // get all results and filter because there is still no other api endpoint
            const resultsFromTestNos = results.filter(r => testNos.indexOf(r.testNo) > -1);
            this.chartData = this.LearnerResultChartService.createDataMultipleFinal(resultsFromTestNos);
        })
    }

    /**
     * @param {number[]} testNos
     */
    createChartMultipleComplete(testNos) {
        this.LearnResultResource.getManyComplete(this.project.id, testNos).then(resultsList => {
            this.chartData = this.LearnerResultChartService.createDataMultipleComplete(resultsList);
        })
    }

    switchToFinal() {
        if (this.selectedChartMode === chartMode.COMPLETE) {
            this.$state.go('statistics.compare', {
                testNos: this.testNos.join(','),
                mode: chartMode.CUMULATED
            });
        } else {
            this.ToastService.info('You are already in the cumulated mode');
        }
    }

    switchToComplete() {
        if (this.selectedChartMode === chartMode.CUMULATED) {
            this.$state.go('statistics.compare', {
                testNos: this.testNos.join(','),
                mode: chartMode.COMPLETE
            });
        } else {
            this.ToastService.info('You are already in the complete mode');
        }
    }

    toggleShowInColumns() {
        this.showInColumns = !this.showInColumns;

        // adjust the size of the charts
        window.setTimeout(() => {
            window.dispatchEvent(new Event('resize'));
        }, 100);
    }
}

export default StatisticsCompareController;