package de.eternalwings.bukkit.testplugin

import de.eternalwings.bukkit.konversation.buildPrompts
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.conversations.Conversable
import org.bukkit.conversations.ConversationFactory
import org.bukkit.plugin.java.JavaPlugin

class TestPlugin : JavaPlugin() {
    private lateinit var factory: ConversationFactory

    override fun onEnable() {
        val prompts = buildPrompts {
            message("Good morning!")
            text("Please give me your name:") { input, context ->
                context["name"] = input
            }
            number("How old are you sir?") { number, context ->
                context["age"] = number.toInt()
            }
            number("What is 1+1?") { number, context ->
                context["tries"] = context["tries"] ?: 0
                if(number != 2) {
                    message("You should check your math.") {
                        val tries: Int = context["tries"]!!
                        context["tries"] = tries + 1
                    }
                    retry()
                }
            }
            confirm("You want to send how many tries you needed?") { result, context ->
                if (result) {
                    val name: String = context["name"]!!
                    val age: Int = context["age"]!!
                    val tries: Int = context["tries"]!!
                    context.forWhom.sendRawMessage("User $name age $age needed $tries attempts.")
                }
            }
            message("Bye bye.")
        }
        factory = ConversationFactory(this)
            .withLocalEcho(true)
            .withModality(true)
            .withEscapeSequence("EXIT")
            .withFirstPrompt(prompts)

        this.getCommand("test")!!.setExecutor(this)
    }

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        factory.buildConversation(sender as Conversable).begin()
        return false
    }
}
