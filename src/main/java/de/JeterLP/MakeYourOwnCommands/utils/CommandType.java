package de.JeterLP.MakeYourOwnCommands.utils;

/**
 * @author TheJeterLP
 */
public enum CommandType {

        ITEM("item"),
        MESSAGE("message"),
        ALIAS("alias"),
        TELEPORT("teleport");

        private final String[] configValues;

        CommandType(String... value) {
                this.configValues = value;
        }

        public String[] getConfigValues() {
                return configValues;
        }

        public boolean isValue(String value) {
                boolean found = false;
                for (String s : configValues) {
                        if (s.equalsIgnoreCase(value)) {
                                found = true;
                                break;
                        }
                }
                return found;
        }

        public static boolean isValid(String value) {
                boolean found = false;
                types:
                for (CommandType type : values()) {
                        for (String s : type.getConfigValues()) {
                                if (s.equalsIgnoreCase(value)) {
                                        found = true;
                                        break types;
                                }
                        }
                }
                return found;
        }

        public static CommandType getByName(String name) {
                for (CommandType type : values()) {
                        for (String s : type.getConfigValues()) {
                                if (s.equalsIgnoreCase(name)) {
                                        return type;
                                }
                        }
                }
                return null;
        }

}
