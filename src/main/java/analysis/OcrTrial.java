package analysis;

import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.Normalizer;
import java.util.LinkedList;
import java.util.Scanner;

import javax.imageio.ImageIO;

import java.awt.Rectangle;

import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

public class OcrTrial {
	public static void main(String[] args) throws IOException, TesseractException {
		//Charger l'image
		File file = new File("IMG_4175.jpeg");
		BufferedImage img = ImageIO.read(file);

		ITesseract tesseract = new Tesseract();
		tesseract.setDatapath("work"); //Fichier de langue (jpn.traineddata)))
		tesseract.setLanguage("fra"); //Spécifiez "japonais" comme langue d'analyse

		try {
			FileWriter fwB = new FileWriter("resultats_brutes.txt");
			BufferedWriter wrB = new BufferedWriter(fwB);

			System.out.println("début analyse");
			
			String str = tesseract.doOCR(img);

			//une analyse
			String strNorm = Normalizer.normalize(str, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "");
			//résultat
			wrB.write(strNorm);
			wrB.close();
			System.out.println("fin écriture résultats brutes");

			// -------------

			FileWriter fwN = new FileWriter("resultats_affinés.txt");
			BufferedWriter wrN = new BufferedWriter(fwN);

			Scanner sc = new Scanner(strNorm);
			LinkedList<String> strLines = new LinkedList<>();

			int i = 0;

			while (sc.hasNextLine()) {
				String tmp = sc.nextLine();
				if (tmp.contains("M.")) {
					tmp = tmp.split("M.")[1];
					strLines.add("M." + tmp);
				}
				if (tmp.contains("Mme")) {
					tmp = tmp.split("Mme")[1];
					strLines.add("Mme" + tmp);
				}
			}

			for (String s : strLines) {
				wrN.write(s);
				wrN.newLine();
			}

			System.out.println("fin écriture résultats affinés");

			wrN.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}



	}
}