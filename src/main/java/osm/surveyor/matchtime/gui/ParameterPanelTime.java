package osm.surveyor.matchtime.gui;

import java.awt.Window;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JRadioButton;
import org.apache.commons.imaging.ImageReadException;
import org.apache.commons.imaging.Imaging;
import org.apache.commons.imaging.common.ImageMetadata;
import org.apache.commons.imaging.formats.jpeg.JpegImageMetadata;
import org.apache.commons.imaging.formats.tiff.TiffImageMetadata;
import org.apache.commons.imaging.formats.tiff.constants.ExifTagConstants;
import osm.surveyor.matchtime.Restamp;
import static osm.surveyor.matchtime.gui.ReStamp.dfjp;
import osm.surveyor.matchtime.gui.restamp.DialogCorectTime;

/**
 * パラメータを設定する為のパネル。
 * この１インスタンスで、１パラメータをあらわす。
 */
public class ParameterPanelTime extends ParameterPanel {
	private static final long serialVersionUID = 9118495619374256843L;
	SimpleDateFormat sdf = (SimpleDateFormat)DateFormat.getDateTimeInstance();
    ParameterPanelImageFile imageFile;  // 基準時刻画像
    
    // 基準時刻の指定グループ (排他選択)
    public ButtonGroup baseTimeGroup = new ButtonGroup();
    public JRadioButton exifBase = null;       // EXIF日時を基準にする／ !(ファイル更新日時を基準にする)
    public JRadioButton fupdateBase = null;    // File更新日時を基準にする／ !(EXIF日時を基準にする)
    
    public JButton updateButton;
    public JButton resetButton;
    Window owner;

    public ParameterPanelTime(
            String label, 
            String text, 
            ParameterPanelImageFile imageFile
    ) {
        super(label, text);
        this.imageFile = imageFile;
        
        // "ボタン[変更...]"
        UpdateButtonAction buttonAction = new UpdateButtonAction(this);
        updateButton = new JButton(i18n.getString("button.update"));
        updateButton.addActionListener(buttonAction);
        this.add(updateButton);
        
        // "ボタン[再設定...]"
        ResetButtonAction resetAction = new ResetButtonAction(this);
        resetButton = new JButton(i18n.getString("button.reset"));
        resetButton.addActionListener(resetAction);
        resetButton.setVisible(false);
        this.add(resetButton);
    }
    
    public ParameterPanelTime setOwner(Window owner) {
        this.owner = owner;
        return this;
    }
    
    public ParameterPanelImageFile getImageFile() {
        return this.imageFile;
    }

    /**
     * [変更...]ボタンのアクション
     */
    class UpdateButtonAction implements java.awt.event.ActionListener
    {
        ParameterPanelTime param;
        
        public UpdateButtonAction(ParameterPanelTime param) {
            this.param = param;
        }
        
        public void actionPerformed(ActionEvent e) {
            fileSelect_Action(param);
            (new DialogCorectTime(param, owner)).setVisible(true);
        }
    }
    
    /**
     * [再設定...]ボタンのアクション
     */
    class ResetButtonAction implements java.awt.event.ActionListener
    {
        ParameterPanelTime paramPanelTime;
        
        public ResetButtonAction(ParameterPanelTime param) {
            this.paramPanelTime = param;
        }
        
        public void actionPerformed(ActionEvent e) {
            fileSelect_Action(paramPanelTime);
        }
    }
    
    /**
     * 画像ファイルが選択されたときのアクション
     * １．ラジオボタンの選択を参照してTEXTフィールドにファイルの「日時」を設定する
     * @param param
     */
    void fileSelect_Action(ParameterPanelTime param) {
        if (imageFile.isEnable()) {
            File timeFile = imageFile.getImageFile();

            // Radio Selecter
            sdf.applyPattern(Restamp.TIME_PATTERN);
            if ((exifBase != null) && exifBase.isSelected()) {
                try {
                    ImageMetadata meta = Imaging.getMetadata(timeFile);
                    JpegImageMetadata jpegMetadata = (JpegImageMetadata)meta;
                    if (jpegMetadata != null) {
                        TiffImageMetadata exif = jpegMetadata.getExif();
                        if (exif != null) {
                            String dateTimeOriginal = exif.getFieldValue(ExifTagConstants.EXIF_TAG_DATE_TIME_ORIGINAL)[0];
                            long lastModifyTime = sdf.parse(dateTimeOriginal).getTime();
                            param.argField.setText(dfjp.format(new Date(lastModifyTime)));
                        }
                        else {
                            param.argField.setText("exif == null");
                        }
                    }
                }
                catch (IOException | ParseException | ImageReadException ex) {}
            }
            else {
                long lastModified = timeFile.lastModified();
                param.argField.setText(sdf.format(new Date(lastModified)));
            }
        }
        else {
            param.argField.setText("");
        }
    }
    
    @Override
    public boolean isEnable() {
        if (this.imageFile.isEnable()) {
            String text = this.argField.getText();
            if (text != null) {
                try {
                    sdf.applyPattern(Restamp.TIME_PATTERN);
                    sdf.parse(text);
                    return true;
                }
                catch (ParseException e) {
                    return false;
                }
            }
        }
        return false;
    }
}
