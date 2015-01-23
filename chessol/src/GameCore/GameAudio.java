/* 
 * Copyright (C) 2015 Wen, Chifeng <https://sourceforge.net/u/daviesx/profile/>
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package GameCore;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

/**
 * 游戏音频
 *
 * @author Wen, Chifeng <https://sourceforge.net/u/daviesx/profile/>
 */
public class GameAudio {

        public static final int c_Captured = 0;
        public static final int c_Go = 1;
        public static final int c_Jiang = 2;
        public static final int c_Seat = 3;
        public static final int c_Select = 4;
        private final File[] m_audio_file;

        public GameAudio() {
                m_audio_file = new File[5];
                m_audio_file[c_Captured] = new File("./res/audio/eat.wav");
                m_audio_file[c_Go] = new File("./res/audio/go.wav").getAbsoluteFile();
                m_audio_file[c_Jiang] = new File("./res/audio/jiang.wav");
                m_audio_file[c_Seat] = new File("./res/audio/seat.wav").getAbsoluteFile();
                m_audio_file[c_Select] = new File("./res/audio/select.wav").getAbsoluteFile();
        }

        public void play(int audio_id) {
                try {
                        AudioInputStream audio
                            = AudioSystem.getAudioInputStream(m_audio_file[audio_id].getAbsoluteFile());
                        AudioFormat format = audio.getFormat();
                        DataLine.Info info = new DataLine.Info(Clip.class, format);
                        Clip clip = (Clip) AudioSystem.getLine(info);
                        clip.open(audio);
                        clip.start();
                } catch (UnsupportedAudioFileException | LineUnavailableException | IOException ex) {
                        Logger.getLogger(GameAudio.class.getName()).log(Level.SEVERE, null, ex);
                }
        }
}
