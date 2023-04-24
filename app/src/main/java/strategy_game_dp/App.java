package strategy_game_dp;

import imgui.ImGui;
import imgui.app.Application;
import imgui.app.Configuration;
import imgui.flag.ImGuiConfigFlags;
import imgui.type.ImString;

public class App extends Application {
  public static void main(String[] args) {
    launch(new App());
  }

  private final ImString myString = new ImString();

  @Override
  protected void initImGui(Configuration config) {
    super.initImGui(config);

    ImGui.getIO().setConfigFlags(ImGuiConfigFlags.DockingEnable);
  }

  @Override
  public void process() {
    ImGui.dockSpaceOverViewport();

    if (ImGui.begin("Hello, world!")) {
      ImGui.inputText("My String", this.myString);
      ImGui.text("Content of My Input: " + this.myString.get());
    }
    ImGui.end();
  }
}
