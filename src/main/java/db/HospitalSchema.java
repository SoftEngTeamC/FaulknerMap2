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
            }
        }
    }
}