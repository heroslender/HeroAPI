<div align="center">
<img src="/logo.jpg" alt="heroslender"/>
</div>

## HeroAPI
Conjunto de APIs para facilitar o desenvolvimento dos teus plugins na API Bukkit.\
Estas APIs podem ser usadas em separado ou em conjunto.
#### API de ItemStack
Esta API é para facilitar a criação de ItemStack com ItemMeta, por exemplo displayName ou lore.
Para visualizar esta API [clique aqui](https://github.com/heroslender/HeroAPI/tree/master/src/main/java/com/heroslender/Menu).

Exemplo:
```Java
ItemStack itemExemplo = new HItem(Material.SKULL_ITEM, "§cNome da skull", "Lore linha 1", "Lore linha 2");
```
#### API de Menu
API para facilitar a criação de menus GUI.
Pode visualizar esta API [clicando aqui](https://github.com/heroslender/HeroAPI/tree/master/src/main/java/com/heroslender/Menu).

Exemplo:
```Java
public class MenuExemplo extends Menu {
    public MenuExemplo() {
        super("§aMenu de §eExemplo", MenuSize.TRES_LINHAS);

        setItem(13, new ItemStack(Material.SKULL_ITEM), clickEvent -> {
            clickEvent.getWhoClicked().sendMessage("Clicaste na skull!");
            clickEvent.getWhoClicked().sendMessage("Parabens!");
        });

        setItem(15, new ItemStack(Material.PAPER));
    }
}
```