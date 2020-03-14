package osm.surveyor.matchtime.gui.restamp;

import java.awt.BorderLayout;
import java.io.File;
import java.util.ArrayList;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import osm.surveyor.matchtime.gui.ReStamp;
import static osm.surveyor.matchtime.gui.ReStamp.i18n;
import osm.surveyor.matchtime.gui.Card;
import osm.surveyor.matchtime.gui.PanelAction;
import osm.surveyor.matchtime.gui.ParameterPanelOutput;
import osm.surveyor.matchtime.gui.ParameterPanelTime;

/**
 * [基準画像（開始/終了）]選択パネル
 * @author yuu
 */
public class CardPerformFile extends Card  implements PanelAction {
	private static final long serialVersionUID = 4781494884268871662L;
    ParameterPanelTime arg1_basetime;
    ParameterPanelTime arg2_basetime;
    ParameterPanelOutput arg_output;        // EXIF & 書き出しフォルダ
    JButton doButton;       // [処理実行]ボタン
    
    /**
     * コンストラクタ
     * @param tabbe parent panel
     * @param arg1_basetime         	// 開始画像の基準時刻:
     * @param arg2_basetime         	// 開始画像の基準時刻:
     * @param arg_output              // EXIF & 書き出しフォルダ
     * @param text
     * @param pre
     * @param next
     */
    public CardPerformFile(
            JTabbedPane tabbe,
            ParameterPanelTime arg1_basetime,
            ParameterPanelTime arg2_basetime,
            ParameterPanelOutput arg_output,
            String text,
            int pre, int next
    ) {
        super(tabbe, text, pre, next);
        this.arg1_basetime = arg1_basetime;
        this.arg2_basetime = arg2_basetime;
        this.arg_output = arg_output;
        
        JPanel argsPanel = new JPanel();
        argsPanel.setLayout(new BoxLayout(argsPanel, BoxLayout.PAGE_AXIS));
        
        // 出力フォルダ
        // 5. 変換先のフォルダを選択してください。
        //    - 変換先フォルダには、書き込み権限と、十分な空き容量が必要です。
        JLabel label5 = new JLabel();
        label5.setText(
            String.format(
                "<html><p>%s</p><ul><li>%s</li></ul>",
                i18n.getString("label.restamp.500"),
                i18n.getString("label.restamp.501")
            )
        );
        argsPanel.add(packLine(label5, new JPanel()));
        argsPanel.add(arg_output);
        argsPanel.add(arg_output.outputOverwite);

        // [処理実行]ボタン
        doButton = new JButton(
            i18n.getString("button.execute"),
            ReStamp.createImageIcon("/images/media_playback_start.png")
        );
        argsPanel.add(doButton);
                
        this.mainPanel.add(argsPanel, BorderLayout.CENTER);

        //{{REGISTER_LISTENERS
        SymAction lSymAction = new SymAction();
        doButton.addActionListener(lSymAction);
        //}}
    }
    
    class SymAction implements java.awt.event.ActionListener {
        @Override
        public void actionPerformed(java.awt.event.ActionEvent event) {
            Object object = event.getSource();
            if (object == doButton) {
            	doButton_Action(event);
            }
        }
    }
    
    /**
     * [実行]ボタンをクリックしたときの動作
     * @param event
     */
    void doButton_Action(java.awt.event.ActionEvent event) {
    	doButton.setEnabled(false);
        
        ArrayList<String> arry = new ArrayList<>();
        File file = arg1_basetime.getImageFile().getImageFile();
        File dir = file.getParentFile();
        arry.add(dir.getAbsolutePath());
        arry.add(file.getName());
        arry.add(arg1_basetime.argField.getText());
        file = arg2_basetime.getImageFile().getImageFile();
        arry.add(file.getName());
        arry.add(arg2_basetime.argField.getText());
        arry.add(arg_output.getText());
        
    	String[] argv = arry.toArray(new String[arry.size()]);
        (new DoRestamp(argv)).setVisible(true);
        
    	doButton.setEnabled(true);
    }

    /**
     *  入力条件が満たされているかどうか
     * @return
     */
    @Override
    public boolean isEnable() {
       return (arg1_basetime.isEnable() && arg2_basetime.isEnable());
    }
    
    @Override
    public void openAction() {
       ; // 何もしない
    }
}
