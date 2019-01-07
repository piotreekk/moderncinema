package pl.piotrek.cinema.view;

public enum ViewList {

    LOGIN{
        @Override
        public String getTitle(){
            return "Login";
        }

        @Override
        public String getFxmlPath(){
            return "/fxml/login.fxml";
        }

    },
    REGISTER{
        @Override
        public String getTitle() {
            return "Register";
        }

        @Override
        public String getFxmlPath() {
            return "/fxml/register.fxml";
        }

    },
    DASHBOARD{
        @Override
        public String getTitle() {
            return "Dashboard";
        }

        @Override
        public String getFxmlPath() {
            return "/fxml/dashboard.fxml";
        }
    },
    IMPORT_MOVIES{
        @Override
        public String getTitle() {
            return "Import Movies";
        }

        @Override
        public String getFxmlPath() {
            return "/fxml/import_movies.fxml";
        }
    },
    MANAGE_SEANCES{
        @Override
        public String getTitle() {
            return "Manage Seances";
        }

        @Override
        public String getFxmlPath() {
            return "/fxml/manage_seances.fxml";
        }
    },
    CHOOSE_SEATS{
        @Override
        public String getTitle() {
            return "Choose Seats";
        }

        @Override
        public String getFxmlPath() {
            return "/fxml/choose_seats.fxml";
        }
    },
    SEANCES{
        @Override
        public String getTitle() {
            return "Seances";
        }

        @Override
        public String getFxmlPath() {
            return "/fxml/seances.fxml";
        }
    },
    AUDITORIUM{
        @Override
        public String getTitle() {
            return "Auditorium";
        }

        @Override
        public String getFxmlPath() {
            return "/fxml/auditorium.fxml";
        }
    },
    ADMIN_HOME{
        @Override
        public String getTitle() {
            return "Admin Home";
        }

        @Override
        public String getFxmlPath() {
            return "/fxml/admin_home.fxml";
        }
    },
    USER_HOME{
        @Override
        public String getTitle() {
            return "User Home";
        }

        @Override
        public String getFxmlPath() {
            return "/fxml/user_home.fxml";
        }
    },
    RESERVATION{
        @Override
        public String getTitle() {
            return "Reservation";
        }

        @Override
        public String getFxmlPath() {
            return "/fxml/reservation.fxml";
        }
    },
    MANAGE_ACCOUNTS{
        @Override
        public String getTitle() {
            return "Manage Accounts";
        }

        @Override
        public String getFxmlPath() {
            return "/fxml/manage_accounts.fxml";
        }
    },
    NEW_EMPLOYEE{
        @Override
        public String getTitle() {
            return "New Employee";
        }

        @Override
        public String getFxmlPath() {
            return "/fxml/new_employee.fxml";
        }
    }
    ;


    public abstract String getTitle();
    public abstract String getFxmlPath();

}
