package de.jeff_media.BestTools;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.Locale;

public class PerformanceMeter {

    Main main;

    PerformanceMeter(Main main) {
        this.main=main;
    }

    long puffer;
    int i=0;
    int max=50;

    int cached=0;
    int uncached=0;

    long start=0;
    long end=0;

    final static int nanoPerMilli=1000000;

    void add(long t,boolean cached) {
        long t2 = System.nanoTime();
        if(!main.measurePerformance) return;
        if(cached) { this.cached++; } else { uncached++; }
        if(start==0) start =t2;
        //main.getLogger().info(String.format("Took %2.4f ms",(t2-t)/(double)nanoPerMilli));
        puffer+=t2-t;
        i++;
        if(i==max) printAndReset();
    }

    private void printAndReset() {
        long end=System.nanoTime();
        double secondsBetween = (end-start) / (double)(nanoPerMilli*1000);
        double calcTime = puffer/(double)nanoPerMilli;
        double calcTimePercent = (calcTime /  (secondsBetween*1000)) * 100;


        main.getLogger().warning(String.format(Locale.US,
                //"Took %5.2f ms on avg. ~%2.4f %%/default tick duration.",avgms,percentOnTick
                "%10.2f ms elapsed, of which BestTools took %5.2f ms or %5.3f %% - %2d / %2d "
                + " queries served by cache (%3d %%)",

                secondsBetween*1000,
                calcTime,
                calcTimePercent,
                cached,
                cached+uncached,
                (int) Math.ceil(cached / (double) (cached+uncached) * 100)
        ));

        ChatColor color = ChatColor.GREEN;
        if(calcTimePercent>=1) color = ChatColor.YELLOW;
        if(calcTimePercent>=2) color = ChatColor.RED;

        ChatColor color2 = ChatColor.GREEN;
        if(calcTimePercent<=20) color2 = ChatColor.YELLOW;
        if(calcTimePercent==0) color2 = ChatColor.RED;

        for(Player p : main.getServer().getOnlinePlayers()) {
            if(p.hasPermission("besttools.debug"))
            p.sendMessage(String.format(
                    "Elapsed: %.2f ms, BestTools: %3.2f ms or %s%2.3f %%§r\n"
                    +"%d / %d queries served by cache %s(%3d %%)\n",
                    secondsBetween*1000,
                    calcTime,
                    color,
                    calcTimePercent,
                    cached,
                    cached+uncached,
                    color2,
                    (int) Math.ceil(cached / (double) (cached+uncached) * 100)
            ));
        }

        i=0;puffer=0;start=0;
    }

}
