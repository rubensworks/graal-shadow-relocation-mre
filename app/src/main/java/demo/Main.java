package demo;

import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.Engine;
import org.graalvm.polyglot.Source;
import org.graalvm.polyglot.Value;

import java.io.IOException;

/**
 * @author rubensworks
 */
public class Main {

    public static void main(String[] args) throws IOException {
        System.out.println("Hello World!");

        Engine engine = Engine
                .newBuilder()
                .option("engine.WarnInterpreterOnly", "false")
                .build();
        Context context = Context
                .newBuilder()
                .engine(engine)
                .build();

        Source source = Source.newBuilder("js", """
function testFunction(a, b) {
    return a + b;
}""", "src.js").build();

        context.eval(source);
        Value testFunction = context.getBindings("js").getMember("testFunction");
        Value ret = testFunction.execute(4, 8);
        System.out.println(ret.toString());
    }
}
