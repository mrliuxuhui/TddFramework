package icons;

import com.intellij.openapi.util.IconLoader;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public interface MyIcons {
    @NotNull Icon EditTest = IconLoader.getIcon("/icons/edit.svg", MyIcons.class);
}
