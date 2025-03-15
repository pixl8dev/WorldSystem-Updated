
package de.cycodly.worldsystem.wrapper;

import de.cycodly.worldsystem.WorldSystem;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.WorldCreator;
import org.bukkit.block.Block;
import org.popcraft.chunky.api.ChunkyAPI;
import org.popcraft.chunky.api.event.task.GenerationCompleteEvent;
import java.util.logging.Level;

import java.util.Objects;

public class AsyncCreatorAdapter implements ICreatorAdapter {

    private final WorldSystem worldSystem = WorldSystem.getInstance();
    private boolean generationComplete = false;

    // Create worlds async
    @Override
    public void create(WorldCreator creator, SystemWorld sw, Runnable r) {
        // Load ChunkyAPI service
        ChunkyAPI chunky = Bukkit.getServer().getServicesManager().load(ChunkyAPI.class);
        String worldName = creator.name();

        if (Bukkit.getWorld(worldName) == null) {
            // Start Chunky world generation task asynchronously
            chunky.startTask(worldName, "square", 0, 0, 500, 500, "concentric");

            // Set up callback for when the generation is complete
            chunky.onGenerationComplete(this::onWSGenComplete);

            new Thread(() -> {
                while (!generationComplete) {
                    try {
                        Thread.sleep(100); // Check every 100ms if generation is complete
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                // Once generation is complete, perform the next actions (on the main thread)
                Bukkit.getScheduler().runTask(worldSystem, () -> {
                    // Perform block operation after generation is complete
                    Block block = Bukkit.getWorld(worldName).getBlockAt(0, -64, 0);
                    block.setType(Material.BEDROCK);
                    if (sw != null) {
                        sw.setCreating(false);  // Mark world creation as complete
                    }
                    r.run();
                });
            }).start();
        } else {
            WorldSystem.logger().log(Level.SEVERE,"World " + worldName + " already exists, no generation.");
            r.run();
        }
    }
    // This method will be triggered when generation is complete
    private void onWSGenComplete(GenerationCompleteEvent event) {
        generationComplete = true;
        Bukkit.getLogger().info("World generation completed for " + event.world());
    }
}