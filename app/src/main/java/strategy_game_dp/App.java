package strategy_game_dp;

import imgui.ImGui;
import imgui.app.Application;
import imgui.app.Configuration;
import imgui.flag.ImGuiConfigFlags;

public class App extends Application {
  public static void main(String[] args) {
    launch(new App());
  }

  private static final int TABLEAU_LIGNES = 8;
  private static final int TABLEAU_COLONNES = 8;

  @Override
  protected void initImGui(Configuration config) {
    super.initImGui(config);

    ImGui.getIO().setConfigFlags(ImGuiConfigFlags.DockingEnable);
  }

  @Override
  public void process() {
    ImGui.dockSpaceOverViewport();
    ImGui.begin("Tableau");

    // Commencer le tableau
    ImGui.beginTable("mon_tableau", TABLEAU_COLONNES);


    // Remplir le tableau avec des cases
    for (int i = 0; i < TABLEAU_LIGNES; i++) {
      ImGui.tableNextRow();
      for (int j = 0; j < TABLEAU_COLONNES; j++) {
        ImGui.tableNextColumn();
        ImGui.setNextItemWidth(50);
        if (ImGui.button("Case " + i + " " + j)) {
          System.out.println("Case " + i + " " + j);
        }
      }
    }

    // Terminer le tableau
    ImGui.endTable();

    // Afficher la fenêtre ImGui
    ImGui.end();
    ImGui.render();
  }
}
