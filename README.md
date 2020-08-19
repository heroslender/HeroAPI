<img src="https://github.com/heroslender.png" alt="Heroslender" title="Heroslender" align="right" height="96" width="96"/>

# HeroAPI

Conjunto de APIs para facilitar o desenvolvimento dos teus plugins na API Bukkit.\
Estas APIs podem ser usadas em separado ou em conjunto.

## API de Configuração YAML

Esta API é para facilitar a criação de configurações com nomes diferentes do padrão(`config.yml`).
Para visualizar esta API [clique aqui](src/main/java/com/heroslender/config/yaml/CustomFileConfiguration.java).

Exemplo:
```Java
CustomFileConfiguration cfg = new CustomFileConfiguration("mensagens", Main.getInstance());

// Metodos para guardar & reload da config
cfg.save();
cfg.reload();

// Inclui todos os metodos padrao da config também
cfg.getString("foo.bar", "Default value");
cfg.getStringList("foo.baz");
```

## API de ItemStack

Esta API é para facilitar a criação de ItemStack com ItemMeta, por exemplo displayName ou lore.
Para visualizar esta API [clique aqui](https://github.com/heroslender/HeroAPI/tree/master/src/main/java/com/heroslender/ItemStack).

Exemplo:
```Java
ItemStack itemExemplo = new HItem(Material.SKULL_ITEM, "§cNome da skull", "Lore linha 1", "Lore linha 2");
```

## API de Menu

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

Uso:
```Java
new MenuExemplo().open(player);
```

### Maven

```xml
<repositories>
    <repository>
        <id>heroslender-repo</id>
        <url>https://nexus.heroslender.com/repository/maven-public/</url>
    </repository>
</repositories>

<dependencies>
    <dependency>
        <groupId>com.heroslender</groupId>
        <artifactId>HeroAPI</artifactId>
        <version>1.0</version>
        <scope>compile</scope>
    </dependency>
</dependencies>
```
