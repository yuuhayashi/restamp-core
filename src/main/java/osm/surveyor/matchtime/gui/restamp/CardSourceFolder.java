package osm.surveyor.matchtime.gui.restamp;

import java.awt.BorderLayout;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import osm.surveyor.matchtime.gui.ReStamp;
import static osm.surveyor.matchtime.gui.ReStamp.i18n;
import osm.surveyor.matchtime.gui.Card;
import osm.surveyor.matchtime.gui.PanelAction;
import osm.surveyor.matchtime.gui.ParameterPanelFolder;

/**
 * [対象フォルダ]設定パネル
 * @author yuu
 */
public class CardSourceFolder extends Card implements PanelAction {
	private static final long serialVersionUID = 5204302830596941052L;
	ParameterPanelFolder arg_srcFolder;    // 対象フォルダ
    
    /**
     * コンストラクタ
     * @param tabbe parent panel
     * @param arg_srcFolder        対象フォルダ
     */
    public CardSourceFolder(JTabbedPane tabbe, ParameterPanelFolder arg_srcFolder) {
        super(tabbe, ReStamp.i18n.getString("tab.100"), -1, 1);
        this.arg_srcFolder = arg_srcFolder;
        this.mainPanel.add(new JLabel(i18n.getString("label.100")), BorderLayout.NORTH);

        JPanel argsPanel = new JPanel();    // パラメータ設定パネル	(上部)
        argsPanel.setLayout(new BoxLayout(argsPanel, BoxLayout.Y_AXIS));
        argsPanel.add(arg_srcFolder);
        this.mainPanel.add(argsPanel, BorderLayout.CENTER);
    }

    /**
     *  入力条件が満たされているかどうか
     * @return
     */
    @Override
    public boolean isEnable() {
       return this.arg_srcFolder.isEnable();
    }

    @Override
    public void openAction() {
       ; // 何もしない
    }
}
