import java.util.*;

public class Terminal implements Prompt {
	private Scanner Keyboard;
	public boolean Suspended = false;
	public Terminal() {
		this.Keyboard = new Scanner(System.in);
	}
	private void BUILDPROCCESS(String cmds) {
		try { new ProcessBuilder("cmd.exe", "/c", cmds).inheritIO().start().waitFor(); }
		catch (Exception e) { System.out.println(e); }
	}
	public void Clear() { BUILDPROCCESS("cls"); }
	public void Pause() { BUILDPROCCESS("pause"); }
	public void Close() {
		try { System.out.close(); Runtime.getRuntime().exec("taskkill /f /im cmd.exe"); Suspended = true; }
		catch (Exception e) { System.out.println(e); }
	}
	@Override
	public boolean PromptBoolean(String s) {
		do {
			System.out.print(String.format("%s (y/n): ", s));
			String Input = Keyboard.nextLine();
			Input = Input.toUpperCase();
			switch (Input) {
				case "Y":
					return true;
				case "N":
					return false;
				default:
					break;
			}
			System.out.println("Please input y or n.");
		} while (true);
	}
	@Override
	public int PromptInteger(String s, int i, int j) {
		do {
			System.out.print(String.format("%s (%d-%d): ", s, i, j));
			String Input = Keyboard.nextLine();
			try {
				int Value = Integer.parseInt(Input);
				if (Value >= i && Value <= j) {
					return Value;	
				} else {
					System.out.println("Please input an integer, by in a range from instruction.");
				}
			} catch (NumberFormatException e) {
				System.out.println("Please input an integer.");
			}
		} while (true);
	}
	@Override
	public String PromptString(String s, String t) {
		System.out.print(String.format("%s (%s): ", s, t));
		return Keyboard.nextLine();
	}
}