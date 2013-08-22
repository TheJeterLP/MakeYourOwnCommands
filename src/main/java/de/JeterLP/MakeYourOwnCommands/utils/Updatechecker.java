package de.JeterLP.MakeYourOwnCommands.utils;

import de.JeterLP.MakeYourOwnCommands.Main;
import java.io.FileNotFoundException;
import java.net.URL;
import java.net.UnknownHostException;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author JeterLP
 */
public class Updatechecker {

    private int currentversion;
    private int newversion;
    private Main main;
    private String newerversion;

    /**
     * <p>This is the constructor needed to get the Main class</p>
     *
     * @param main
     */
    public Updatechecker(Main main) {
        this.main = main;
    }

    /**
     * <p>This method looks for new updates on BukkitDev</p>
     *
     * @param currentVersion
     * @return currentversion
     * @throws Exception
     */
    private int updateCheck(int currentVersion) throws Exception {
        String pluginUrlString = "http://dev.bukkit.org/server-mods/Simple-info2/files.rss";
        try {
            URL url = new URL(pluginUrlString);
            Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(url.openConnection().getInputStream());
            doc.getDocumentElement().normalize();
            NodeList nodes = doc.getElementsByTagName("item");
            Node firstNode = nodes.item(0);
            if (firstNode.getNodeType() == 1) {
                Element firstElement = (Element) firstNode;
                NodeList firstElementTagName = firstElement.getElementsByTagName("title");
                Element firstNameElement = (Element) firstElementTagName.item(0);
                NodeList firstNodes = firstNameElement.getChildNodes();
                this.newerversion = firstNodes.item(0).getNodeValue().replace("MakeYourOwnCommands ", "");
                return Integer.valueOf(firstNodes.item(0).getNodeValue().replace("MakeYourOwnCommands ", "").replaceAll("\\.", ""));
            }
        } catch (UnknownHostException e) {
            MYOClogger.log(MYOClogger.Type.ERROR, "Error on update-check, maybe you have an internet protection?");
        } catch (Exception e) {
            MYOClogger.log(MYOClogger.Type.ERROR, "Error on update-check \n" + e.getMessage() + "\n" + e);
            e.printStackTrace();
        }
        return currentVersion;
    }

    /**
     * <p>This method starts the update-checker and notifies that a new version
     * is released</p>
     */
    public void checkUpdate() {
        if (main.getConfig().getBoolean("CheckForUpdates", true)) {
            currentversion = Integer.valueOf(main.getDescription().getVersion().replaceAll("\\.", "").replaceAll("-SNAPSHOT", ""));
            main.getServer().getScheduler().runTaskTimerAsynchronously(main, new Runnable() {
                @Override
                public void run() {
                    try {
                        newversion = updateCheck(currentversion);
                        if (newversion > currentversion) {
                            System.out.println(main.prefix + "version " + newerversion + " has been released! You can download it at http://dev.bukkit.org/bukkit-plugins/simple-info2/");
                        } else if (currentversion > newversion) {
                            System.out.println(main.prefix + "your version (" + main.getDescription().getVersion() + ") is higher than the newest version on bukkitDev (" + newerversion + "), do you use a development build?");
                        }
                    } catch (FileNotFoundException e) {
                        MYOClogger.log(MYOClogger.Type.ERROR, "Error on update-check, maybe you have an internet protection?");
                    } catch (Exception e) {
                        MYOClogger.log(MYOClogger.Type.ERROR, "Error on update-check \n" + e.getMessage() + "\n" + e);
                        e.printStackTrace();
                    }
                }
            }, 0, 36000);
        }
    }
}
