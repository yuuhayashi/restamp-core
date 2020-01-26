package osm.surveyor.matchtime.gui;

import javax.swing.JCheckBox;
import javax.swing.JFileChooser;

@SuppressWarnings("serial")
public class ParameterPanelOutput extends ParameterPanelFolder
{
    public JCheckBox outputOverwite;	// _OVERWRITE_TO_SOURCE
    ParameterPanelFolder srcPanelFolder;
    
    /**
     * コンストラクタ
     * ディレクトリのみ選択可能なダイアログ
     * @param label
     * @param text 
     */
    public ParameterPanelOutput(String label, String text) {
        super(label, text, JFileChooser.DIRECTORIES_ONLY);
    }

    /**
     * チェックボックス "入力ファイルに上書きする"
     * @param srcPanelFolder    // 
     */
    public void addCheckOverwriteToSource(ParameterPanelFolder srcPanelFolder) {
        this.srcPanelFolder = srcPanelFolder;
        boolean selected = false;
        outputOverwite = new JCheckBox(i18n.getString("label.580"), selected);
        outputOverwite.setEnabled(true);
        outputOverwite.addActionListener(new ChangeOverwriteAction());
    }

    /**
     * checkbox[入力ファイルに上書き]を変更した場合のアクション
     * 	OFF → IMG出力フォルダのフィールドを有効にする
     *  ON → IMG出力フォルダのフィールドを無効にする
     * @param event
     */
    class ChangeOverwriteAction implements java.awt.event.ActionListener {
        @Override
        public void actionPerformed(java.awt.event.ActionEvent event) {
            Object object = event.getSource();
            if (object == outputOverwite) {
                if (outputOverwite.isSelected()) {
                    String text = srcPanelFolder.argField.getText();
                    argField.setText(text);
                }
            }
        }
    }
}