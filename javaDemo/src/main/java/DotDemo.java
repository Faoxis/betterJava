public class DotDemo {

    public static class Dot {
        private final int x;
        private final int y;

        public Dot(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public String accept(Visitor visitor) {
            return visitor.visit(this);
        }

        public int getX() { return x; }
        public int getY() { return y; }
    }

    public interface Visitor {
        String visit(Dot dot);
    }

    public static class JsonVisitor implements Visitor {
        @Override
        public String visit(Dot dot) {
            return String
                    .format("" +
                            "{" +
                                    "\"x\"=%d, " +
                                    "\"y\"=%d " +
                            "}",
                    dot.getX(), dot.getY());
        }
    }

    public static class XMLVisitor implements Visitor {
        @Override
        public String visit(Dot dot) {
            return "<dot>" + "\n" +
                    "    <x>" + dot.getX() + "</x>" + "\n" +
                    "    <y>" + dot.getY() + "</y>" + "\n" +
                    "</dot>";
        }
    }

    public static void main(String[] args) {
        Dot dot = new Dot(1, 2);

        System.out.println("-------- JSON -----------");
        System.out.println(dot.accept(new JsonVisitor()));

        System.out.println("-------- XML ------------");
        System.out.println(dot.accept(new XMLVisitor()));
    }
}
