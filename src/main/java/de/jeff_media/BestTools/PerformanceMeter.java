package de.jeff_media.BestTools;

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

        String out = String.format(Locale.US,
                //"Took %5.2f ms on avg. ~%2.4f %%/default tick duration.",avgms,percentOnTick
                "%6.2f %s elapsed, of which BestTools took %2.2f ms or %1.3f %% - "
                + "%d %% of queries served by cache",

                        //%d %% of queries served from cache",
                /*secondsBetween > 1 ? secondsBetween : secondsBetween*1000,
                secondsBetween > 1 ? "s" : "ms",*/
                secondsBetween*1000,"ms",
                calcTime,
                calcTimePercent,
                (int) Math.ceil(cached / (double) (cached+uncached) * 100)
                //(int) Math.ceil((cached / (double) ((cached+uncached)*100)))
        );

        //main.getLogger().warning(out);
        main.getServer().broadcastMessage(out);

        i=0;puffer=0;start=0;
    }

}
