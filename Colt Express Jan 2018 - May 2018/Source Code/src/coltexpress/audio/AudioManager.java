package coltexpress.audio;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;

public class AudioManager implements Runnable {
	private static File[] allAudioFiles;
	private static String[] audioFiles = {"fire.wav", "money.wav", "music.wav", "punch.wav", "train.wav", "trainWhistle.wav"};
	
	// For playing multiple Times
	public Thread audioThread;
	private static int i, numTimes;
	private static long delay;
	private static double intensity;
	
	public AudioManager() {
		audioThread = new Thread(this);
	}
	
	private static int getIndex(String name) {
		for (int i = 0; i < audioFiles.length; i++) {
			if (audioFiles[i].contains(name))
				return i;
		}
		return -1;
	}
	
	public static void playAudioNTimes(String name, double intensity, int n, long delay) {
		i = getIndex(name);
		AudioManager.intensity = intensity;
		numTimes = n;
		AudioManager.delay = delay;
		
		(new AudioManager()).audioThread.start();
	}
	
	public static void loopAudio(String name, double intensity) {
		int i = getIndex(name);
		
		AudioInputStream audioIn = null;
		try {
			audioIn = AudioSystem.getAudioInputStream(allAudioFiles[i]);
			Clip cli = AudioSystem.getClip();
			cli.open(audioIn);
			FloatControl gainControl = (FloatControl) cli.getControl(FloatControl.Type.MASTER_GAIN);
			double gain = intensity;
			float dB = (float)(Math.log(gain) / Math.log(10.0) * 20.0);
			gainControl.setValue(dB);
			cli.loop(Clip.LOOP_CONTINUOUSLY);
		} catch (Exception e) {}
	}
	
	public static void readAllAudio() throws IOException {
		allAudioFiles = new File[audioFiles.length];
		
		for (int i = 0; i < audioFiles.length; i++) {
			allAudioFiles[i] = new File("Files/Audio/" + audioFiles[i]);
//			System.out.println(allAudioFiles[i].exists());
		}
	}

	@Override
	public void run() {
		while (numTimes-- > 0) {
			try {
				AudioInputStream audioIn = AudioSystem.getAudioInputStream(allAudioFiles[i]);
				Clip cli = AudioSystem.getClip();
				cli.open(audioIn);
				FloatControl gainControl = (FloatControl) cli.getControl(FloatControl.Type.MASTER_GAIN);
				double gain = intensity;
				float dB = (float)(Math.log(gain) / Math.log(10.0) * 20.0);
				gainControl.setValue(dB);
				cli.start();
			} catch (Exception e) {}
			try { Thread.sleep(delay); } catch(Exception e) {}
		}
	}
}
