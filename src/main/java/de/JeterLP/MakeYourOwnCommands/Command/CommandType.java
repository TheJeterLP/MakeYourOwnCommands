package de.JeterLP.MakeYourOwnCommands.Command;

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

        /**
         * Returns all possible names for the Type in the config.
         * @return configValue 
         */
        public String[] getConfigValues() {
                return configValues;
        }

        /**
         * Checks if the given value is used for the CommandType.
         * @param value: The value to check.
         * @return true: if it's used from the CommandType.
         */
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

        /**
         * Checks if the given value is a valid CommandType.
         * @param value: The value to check.
         * @return true: If the given value is valid. 
         */
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

        /**
         * Searches for a CommandType which uses the given name as a value.
         * @param name: The value to search for
         * @return CommandType 
         * Returns null if no possible CommandType was found.
         */
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
