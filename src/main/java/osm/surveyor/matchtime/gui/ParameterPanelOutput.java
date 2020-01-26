package osm.surveyor.matchtime.gui;

import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import osm.surveyor.matchtime.AppParameters;

@SuppressWarnings("serial")
public class ParameterPanelOutput extends ParameterPanelFolder
{
    JCheckBox outputOverwite;	// GPX_OVERWRITE_TO_SOURCE
    
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
     * @param label
     * @param params 
     */
    public void addCheckOverwriteToSource(String label, AppParameters params) {
        boolean selected = false;
        if (params.getProperty(AppParameters.OUTPUT_OVERWRITE_TO_SOURCE).equals("true")) {
            selected = true;
        }
        outputOverwite = new JCheckBox(label, selected);
        outputOverwite.setEnabled(true);
    }

    /**
     * checkbox[入力ファイルに上書き]を変更した場合のアクション
     * 	ON ー＞ IMG出力フォルダのフィールドを有効にする
     *  OFF -> IMG出力フォルダのフィールドを無効にする
     * @param event
     */
    /*
    
    class ChangeOverwriteAction implements java.awt.event.ActionListener {
        @Override
        public void actionPerformed(java.awt.event.ActionEvent event) {
            Object object = event.getSource();
            if (object == outputOverwite) {
                setEnabled(outputIMG.isEnabled());
            }
        }
    }
    */
}