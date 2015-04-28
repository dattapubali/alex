package de.learnlib.alex.utils;

import de.learnlib.algorithms.discriminationtree.hypothesis.HState;
import de.learnlib.discriminationtree.DTNode;
import de.learnlib.discriminationtree.DiscriminationTree;
import net.automatalib.words.Word;

/**
 * Custom serializer that converts a (LearnLib) Discrimination Tree into nice JSON.
 */
public final class DiscriminationTreeSerializer {

    private DiscriminationTreeSerializer() {
    }

    /**
     * Serializes the discrimination tree of the discrimination tree algorithm into JSON.
     *
     * @param tree
     *         The tree to convert into nice JSON.
     * @return The JSON string of the given tree.
     */
    public static String toJSON(DiscriminationTree<String, Word, HState> tree) {
        return toJSON(tree.getRoot());
    }

    private static String toJSON(DTNode<String, Word, HState> node) {
        StringBuilder result = new StringBuilder();
        result.append('{');

        if (node.getParentOutcome() != null) {
            result.append("\"edgeLabel\": \"");
            result.append(node.getParentOutcome());
            result.append("\",");
        }

        if (node.isLeaf()) {
            result.append("\"data\": \"");
            result.append(node.getData());
            result.append('"');
        } else {
            result.append("\"discriminator\": \"");
            result.append(node.getDiscriminator());
            result.append("\",");

            result.append("\"children\": [");
            node.getChildEntries().forEach(entry -> {
                DTNode child = entry.getValue();
                result.append(toJSON(child));
                result.append(",");
            });

            result.setLength(result.length() - 1); // remove last ','
            result.append(']');
        }

        result.append('}');
        return result.toString();
    }
}