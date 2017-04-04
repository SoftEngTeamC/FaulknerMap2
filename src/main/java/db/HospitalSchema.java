package db;

/**
 * Created by Gina on 3/27/17.
 */
/*
    This class holds all the database column names of the tables in hospital
    Using this class helps prevent typos/mistakes when calling the database
 */

public class HospitalSchema {

    // Hospital service
    public class HospitalServiceSchema {
        public final class HospitalServiceTable {
            public static final String NAME = "hospitalServices";

            public final class Cols {
                public static final String ID = "id";
                public static final String NAME = "name";
                public static final String LOCATION = "location";
                public static final String NODEID = "nodeId";
            }
        }
    }

    public class HospitalProfessionalSchema {
        public final class HospitalProfessionalTable {
            public static final String NAME = "hospitalProfessionals";

            public final class Cols {
                public static final String ID = "id";
                public static final String NAME = "name";
                public static final String TITLE = "title";
                public static final String LOCATION = "location";
                public static final String NODEID = "nodeId";
            }
        }
    }

    public class NodeSchema {
        public final class NodeTable {
            public static final String NAME = "hospitalNodes";

            public final class Cols {
                public static final String ID = "id";
                public static final String NAME = "name";
                public static final String X = "x";
                public static final String Y = "y";
                public static final String Z = "z";
            }
        }
    }

    public class EdgeSchema {
        public final class EdgeTable {
            public static final String NAME = "hospitalEdges";

            public final class Cols {
                public static final String ID = "id";
                public static final String FROM_NODE = "from_node";
                public static final String TO_NODE = "to_node";
                public static final String LENGTH = "length";
                public static final String DISABLED = "disabled";
            }

            public final class Constraints {
                public static final String FROM_NODE_CON = "from_node_con";
                public static final String TO_NODE_CON = "to_node_con";
            }
        }
    }
}