package osm.surveyor.matchtime.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;

@SuppressWarnings("serial")
public class ParameterPanelGpx extends ParameterPanel implements ActionListener
{
    JFileChooser fc;
    JButton selectButton;
    public JCheckBox noFirstNode;      // CheckBox: "セグメント'trkseg'の最初の１ノードは無視する。"
    public JCheckBox gpxReuse;         // CheckBox: "生成されたGPXファイル（ファイル名が'_.gpx'で終わるもの）も変換の対象にする"
    
    /**
     * コンストラクタ
     * @param label
     * @param text 
     */
    public ParameterPanelGpx(String label, String text) {
        super(label, text);

        // "選択..."
        selectButton = new JButton(
                i18n.getString("button.select"), 
                ReStamp.createImageIcon("/images/Open16.gif")
        );
        selectButton.addActionListener(this);
        this.add(selectButton);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == selectButton){
            System.out.println("ParameterPanelGpx.actionPerformed(openButton)");
            File sdir = new File(this.argField.getText());
            if (sdir.exists()) {
                this.fc = new JFileChooser(sdir);
            }
            else {
                this.fc = new JFileChooser();
            }
            this.fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
            this.fc.addChoosableFileFilter(new GpxAndFolderFilter());
            this.fc.setAcceptAllFileFilterUsed(false);

            int returnVal = this.fc.showOpenDialog(ParameterPanelGpx.this);

            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = this.fc.getSelectedFile();
                this.argField.setText(file.getAbsolutePath());
            }
        }
    }

    public File getGpxFile() {
        if (isEnable()) {
            return new File(getText());
        }
        return null;
    }
    
    public boolean isNoFirstNodeSelected() {
        return (noFirstNode != null) && noFirstNode.isSelected();
    }
    
    public boolean isGpxReuseSelected() {
        return (gpxReuse != null) && gpxReuse.isSelected();
    }
        
    /**
     * このフィールドに有効な値が設定されているかどうか
     * @return 
     */
    @Override
    public boolean isEnable() {
        String text = this.argField.getText();
        if (text != null) {
            File file = new File(text);
            if (file.exists()) {
                if (file.isFile()) {
                    String name = file.getName().toUpperCase();
                    if (name.endsWith(".GPX")) {
                        return true;
                    }
                }
                else if (file.isDirectory()) {
                    return true;
                }
            }
        }
        return false;
    }
}
