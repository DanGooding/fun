package fun.values;// TODO this is a bad solution
// this has no shared behaviour with a function:
//   (except perhaps debug printing)
// even a fun.values.Value interface seems pointless
// just have a 'type' tag ?

import fun.eval.EvaluationException;

public interface Value {

    default void fullyForce() throws EvaluationException {
        // by default, values have nothing to force,
        // composite values however do
    }

}
