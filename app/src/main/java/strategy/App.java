package strategy;

import imgui.ImGui;
import imgui.app.Application;
import imgui.app.Configuration;
import imgui.flag.ImGuiCol;
import imgui.flag.ImGuiConfigFlags;
import imgui.flag.ImGuiStyleVar;

public class App extends Application {
  private static final int HEIGHT = 8;
  private static final int WIDTH = 8;

  public static void main(String[] args) {
    Game.createInstance(WIDTH, HEIGHT);
    Game.getInstance().play();
    // launch(new App());
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
    ImGui.beginTable("mon_tableau", WIDTH);

    // Remplir le tableau avec des cases carrées de 50px x 50px
    for (int i = 0; i < HEIGHT; i++) {
      ImGui.tableNextRow();
      for (int j = 0; j < WIDTH; j++) {
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
