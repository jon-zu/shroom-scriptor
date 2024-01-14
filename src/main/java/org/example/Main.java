package org.example;

import org.example.fmt.ItemId;
import org.example.fmt.Menu;
import org.example.fmt.ShroomFmtProcessor;

import java.util.List;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    static class MyScriptApi implements IScriptApi {

        private String name;
        private int money;
        private int firstItemId;

        MyScriptApi(String name, int money, int firstItemId) {
            this.name = name;
            this.money = money;
            this.firstItemId = firstItemId;
        }


        @Override
        public String getName() {
            return this.name;
        }

        @Override
        public void giveMoney(int amount) {
            this.money += amount;
        }

        @Override
        public void hasItem(int itemId) {
            this.firstItemId = itemId;
        }
    }

    public static class ExampleScript extends ScriptRunnable {
        public ExampleScript(IScriptApi api) {
            super(api);
        }

        @Override
        public void exec(IScriptApi api) throws InterruptedException {
            // TODO put this into the handler
            this.waitStart();

            this.sendMessage(STR."Hello \{api.getName()}");
            var money = this.sendInputNumber("Enter how much money you want to receiver");
            api.giveMoney(money);

            var job = this.sendSelect(List.of(new String[]{
                    "thief",
                    "warrior",
                    "mage"
            }));

            this.sendMessage(STR."You are a \{job}");
            this.sendEnd("Goodbye");
        }
    }

    static ShroomFmtProcessor SHROOM_FMT = new ShroomFmtProcessor();

    public static void main(String[] args) throws InterruptedException {
        String res = SHROOM_FMT."This item is required for the quest: \{new ItemId(1000).withIconText()}";
        System.out.println(res);

        var reward = new Menu(List.of(new  ItemId[]{
            new ItemId(1000).withIconText(),
            new ItemId(1001).withIconText(),
            new ItemId(1002).withIconText()
        }));

        res = SHROOM_FMT."""
            You got the item required for the quest:\{new ItemId(1000).withIconText()}
            and you can select select between multiple rewards:
            \{reward}
        """;
        System.out.println(res);


        var api = new MyScriptApi("John", 100, 1);
        var script = new ExampleScript(api);
        var handle = new ScriptHandle(script);

        var actionSeq = new NpcActions[] {
            NpcActions.NEXT,
            NpcActions.INPUT_NUMBER,
            NpcActions.SELECT,
            NpcActions.NEXT
        };

        var ix = 0;

        handle.start();;

        while(handle.isRunning()) {
            if(ix == actionSeq.length) {
                break;
            }
            var action = actionSeq[ix];
            handle.step(action);
            System.out.println(handle.getMessage());
            ix++;
        }
    }
}