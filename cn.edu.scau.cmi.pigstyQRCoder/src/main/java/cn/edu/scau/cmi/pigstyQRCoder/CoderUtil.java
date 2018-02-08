package cn.edu.scau.cmi.pigstyQRCoder;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Shape;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Hashtable;
import javax.imageio.ImageIO;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
public class CoderUtil {
	
	public static final boolean QR_CODE = true;
	public static final boolean ONE_D_CODE = false;
	public static final int SMALL_SIZE = 1; 
	public static final int MID_SIZE = 2;
	public static final int BIG_SIZE = 3; 
	
	// LOGO宽度
	private static int logoWidth;
	// LOGO高度
	private static int logoHeight;
	
	private static final String CHARSET = "utf-8";
	
	// 条码尺寸
	private static int codeWidth;
	private static int codeHeight;
	
	//条码格式
	private static BarcodeFormat barcodeFormat;
	
	private static double increRate;
	private static int fontSize;
	

	
	private static void initBarcodeParameter(int size) {
		increRate = 1.8;
		fontSize = 10*size;
		logoHeight = logoWidth = 0;
		codeWidth = 150*size;
		codeHeight = 30*size;
		barcodeFormat = BarcodeFormat.CODE_128;
	}
	
	private static void initQRCodeParameter(int size) {
		increRate = 1+(double)size/10;
		fontSize = 10*size;
		logoHeight = logoWidth = 30*size;
		codeWidth = codeHeight = 170*size;
		barcodeFormat = BarcodeFormat.QR_CODE;
	}
	

	/**
	 * 核心方法，创建二维条码图片
	 * @param content 条码内容
	 * @param logoImg 二维码logo
	 * @param isCompressLogoImg 
	 * @param memo 条码下面文字
	 * @return
	 * @throws Exception
	 */
	private static BufferedImage createQRImage(
			String content, File logoImg, boolean isCompressLogoImg, String memo) throws Exception {
		
		Hashtable<EncodeHintType, Object> hints = new Hashtable<EncodeHintType, Object>();
		
		//设置纠错等级
		hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
		//设置字符集
		hints.put(EncodeHintType.CHARACTER_SET, CHARSET);
		//设置外边距
		hints.put(EncodeHintType.MARGIN, 1);
//		二维条码的区域
//		获得编码后的位矩阵
		BitMatrix bitMatrix = new MultiFormatWriter().
				encode(content, barcodeFormat, codeWidth, codeHeight, hints);
		

		if(memo == null) {
			increRate = 1;
		}
		

		BufferedImage image = new BufferedImage(codeWidth, (int)(codeHeight*increRate), BufferedImage.TYPE_INT_RGB);
		
		Graphics2D graphics2d = image.createGraphics();
		graphics2d.setColor(Color.white);
		graphics2d.fillRect(0, 0, image.getWidth(), image.getHeight());
		
		if(memo != null) {
			appendMemoUnderQR(image, memo);
		}
	
		graphics2d.setColor(Color.BLACK);
		for (int x = 0; x < codeWidth; x++) {
			for (int y = 0; y < codeHeight; y++) {
				if(bitMatrix.get(x, y)){
					graphics2d.fillRect(x, y, 1, 1);
				}
			}
		}
		graphics2d.dispose();
//		如果有logoImg,就在生成的二维条码图片中添加logo图片
		if (logoImg != null){
			insertImageIntoQR(image, logoImg, isCompressLogoImg);
		}
		return image;
	}
	
	
//	在生成的二维条码下面插入备注信息
	private static void appendMemoUnderQR(BufferedImage image,String memo) throws Exception {
		int width = image.getWidth();
		int height = image.getHeight();
		float memoX = width/20;	
	    float memoY = codeHeight + (height-codeHeight)/2;
		Graphics2D graphics2D = image.createGraphics();
		graphics2D.setColor(Color.black);
		graphics2D.setFont(new Font(null, Font.BOLD, fontSize));
		graphics2D.setBackground(Color.white);
		graphics2D.drawString(memo,  memoX,	 memoY);
		graphics2D.dispose();
	}
	
	
//	在生成的二维条码中间插入图片
	private static void insertImageIntoQR(BufferedImage sourceBufferedImage, File logImgFile, boolean needCompress) throws Exception {
//		File file = new File(logImg);
		if (!logImgFile.exists()) {
			System.err.println(""+logImgFile+"   该文件不存在！");
			return;
		}
		Image src = ImageIO.read(logImgFile);
		int width = src.getWidth(null);
		int height = src.getHeight(null);
		if (needCompress) { // 压缩LOGO
			if (width > logoWidth) {
				width = logoWidth;
			}
			if (height > logoHeight) {
				height = logoHeight;
			}
			Image image = src.getScaledInstance(width, height,	Image.SCALE_SMOOTH);
			BufferedImage tag = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
			Graphics g = tag.getGraphics();
			g.drawImage(image, 0, 0, null); // 绘制缩小后的图
			g.dispose();
			src = image;
		}
		// 插入LOGO
		Graphics2D graph = sourceBufferedImage.createGraphics();
		int x = (codeWidth - width) / 2;
		int y = (codeWidth - height) / 2;
		graph.drawImage(src, x, y, width, height, null);
		
		
		Shape shape = new RoundRectangle2D.Float(x, y, width, width, 6, 6);
		graph.draw(shape);
		graph.dispose();
	}


	private static void mkdirs(String destPath) {
		File file =new File(destPath);    
		//当文件夹不存在时，mkdirs会自动创建多层目录，区别于mkdir．(mkdir如果父目录不存在则会抛出异常)
		if (!file.exists() && !file.isDirectory()) {
			file.mkdirs();
		}
	}

//	利用内容、QR目录、文件、二维码中插入的目录、是否需要压缩创建二维码,design by 梁早清
	public static  BufferedImage encode(String qrContent, File logImgFile, boolean needCompress, String memo, boolean codeFormat, int size) throws Exception {
		if(codeFormat) {
			initQRCodeParameter(size);
		}else {
			initBarcodeParameter(size);
		}
		return createQRImage(qrContent, logImgFile, needCompress, memo);
	}
	
	public static void writeImage(String qrContent,File logImgFile,String memo, File targetpath, String imageFormat, boolean codeFormat) throws Exception {
		if(codeFormat) {
			initQRCodeParameter(MID_SIZE);
		}else {
			initBarcodeParameter(BIG_SIZE);
		}
		BufferedImage bufferedImage = createQRImage(qrContent, logImgFile, true, memo);
		ImageIO.write(bufferedImage, imageFormat, targetpath);
	}

}
