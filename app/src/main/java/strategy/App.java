package strategy;

import imgui.ImGui;
import imgui.app.Application;
import imgui.app.Configuration;
import imgui.flag.ImGuiCol;
import imgui.flag.ImGuiConfigFlags;
import imgui.flag.ImGuiStyleVar;
import strategy.producible.unit.Unit;
import strategy.world.Inventory;
import strategy.world.WorldMap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class App extends Application {
  private static final int TABLEAU_LIGNES = 8;
  private static final int TABLEAU_COLONNES = 8;

  public static void main(String[] args) {
    launch(new App());
  }

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

    // Remplir le tableau avec des cases carrées de 50px x 50px
    for (int i = 0; i < TABLEAU_LIGNES; i++) {
      ImGui.tableNextRow();
      for (int j = 0; j < TABLEAU_COLONNES; j++) {
        ImGui.tableNextColumn();
        // Set a height for each row
        // mettre en blanc
        ImGui.pushStyleColor(ImGuiCol.Button, 1.0f, 1.0f, 1.0f, 1.0f);
        ImGui.popStyleColor();
        if (i == 3 && j == 3) {
          // changer la couleur de la case en Rouge
          ImGui.pushStyleColor(ImGuiCol.Button, 1.0f, 0.0f, 0.0f, 1.0f);
          ImGui.popStyleColor();
          // créer un button
          if (ImGui.button("R", 50, 50)) {
            // changer taille button en 50px par 50px
            ImGui.pushStyleVar(ImGuiStyleVar.ButtonTextAlign, 5000, 5000);
            ImGui.popStyleVar();
          }
        } else if (i == 2 && j == 2) {
          // changer la couleur de la case en Vert
          ImGui.pushStyleColor(ImGuiCol.Button, 0.0f, 1.0f, 0.0f, 1.0f);
          // créer un button
          if (ImGui.button("F", 50, 50)) {
            // changer taille button en 50px par 50px
            ImGui.pushStyleVar(ImGuiStyleVar.ButtonTextAlign, 5000, 5000);
            ImGui.popStyleVar();
          }
          ImGui.popStyleColor();
        } else {
          // créer bouton avec le numéro de la case
          if (ImGui.button("", 50, 50)) {
            // mettre en blanc
            ImGui.pushStyleColor(ImGuiCol.Button, 1.0f, 1.0f, 1.0f, 1.0f);
            ImGui.popStyleColor();
          }
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
