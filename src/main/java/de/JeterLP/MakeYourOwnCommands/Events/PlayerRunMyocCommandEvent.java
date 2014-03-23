package de.JeterLP.MakeYourOwnCommands.Events;

import de.JeterLP.MakeYourOwnCommands.Command.Command;
import de.JeterLP.MakeYourOwnCommands.Command.CommandType;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

/**
 * @author TheJeterLP
 */
public class PlayerRunMyocCommandEvent extends PlayerEvent implements Cancellable {

        private boolean cancelled = false;
        private final Command cmd;
        private static final HandlerList handlers = new HandlerList();

        public PlayerRunMyocCommandEvent(Player who, Command cmd) {
                super(who);
                this.cmd = cmd;
        }

        /**
         * Bukkit Method.
         *
         * @return handlers
         */
        @Override
        public HandlerList getHandlers() {
                return handlers;
        }

        /**
         * Bukkit Method.
         *
         * @return handlers
         */
        public static HandlerList getHandlerList() {
                return handlers;
        }

        /**
         * Gets the executed {@link de.JeterLP.MakeYourOwnCommands.Command.Command}.
         *
         * @return cmd
         */
        public Command getCommand() {
                return cmd;
        }

        /**
         * gets the {@link de.JeterLP.MakeYourOwnCommands.Command.CommandType} of the executed command.
         *
         * @return CommandType
         */
        public CommandType getType() {
                return cmd.getType();
        }

        /**
         * Checks if the event was cancelled.
         *
         * @return cancelled
         */
        @Override
        public boolean isCancelled() {
                return cancelled;
        }

        /**
         * Changes the status of the event. If bln is true, the event will not be executed.
         *
         * @param bln: Sets cancelled to bln
         */
        @Override
        public void setCancelled(boolean bln) {
                cancelled = bln;
        }

}
