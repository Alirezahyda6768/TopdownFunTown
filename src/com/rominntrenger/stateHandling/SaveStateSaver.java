package com.rominntrenger.stateHandling;

import com.rominntrenger.main.RomInntrenger;
import com.rominntrenger.objects.WaveManager;
import com.rominntrenger.objects.player.Player;
import com.rominntrenger.stateHandling.DAO.MetaDAO;
import com.rominntrenger.stateHandling.DAO.PlayerDAO;
import com.rominntrenger.stateHandling.DAO.WaveManagerDAO;

import java.io.*;

public class SaveStateSaver {


    private static void savePlayer(Player p, MetaDAO meta) {
        meta.players.add(new PlayerDAO(p));
    }

    private static void saveWaveManager(WaveManager wm, MetaDAO meta) {
        meta.waveManager = new WaveManagerDAO(wm);
    }

    public static void save(RomInntrenger rom) {

        MetaDAO meta = new MetaDAO();
        for (Player p : rom.players) {
            savePlayer(p, meta);
        }

        saveWaveManager(WaveManager.getInstance(), meta);

        try (ObjectOutputStream oos = new ObjectOutputStream(
            new FileOutputStream(new File("./myFile.data")))) {
            // no need to specify members individually
            oos.writeObject(meta);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

}