package com.flyingwillow.tdd.service;

import com.flyingwillow.tdd.domain.InterfaceMetaInfo;
import com.flyingwillow.tdd.domain.InterfaceMetaType;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.intellij.facet.Facet;
import com.intellij.facet.FacetManager;
import com.intellij.openapi.components.Service;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ModuleRootManager;
import com.intellij.openapi.vfs.VfsUtil;
import com.intellij.openapi.vfs.VfsUtilCore;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiJavaFile;
import com.intellij.psi.util.PsiUtilCore;
import lombok.extern.slf4j.Slf4j;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeModel;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Service
public final class InterfaceMetaService {

    private static final Map<String, String> ERROR_MSG = ImmutableMap.<String, String>builder()
            .put("-1", "本工程不包含spring web模块,请确认所有controller是否放置在xxx.controller包下")
            .put("2", "")
            .build();

    private static final InterfaceNameReader reader = InterfaceNameReader.INSTANCE;

    public List<InterfaceMetaInfo> selectAll(Project project) {
        return ImmutableList.<InterfaceMetaInfo>builder()
                .add(new InterfaceMetaInfo("1", "第一级", "0", 1, false, InterfaceMetaType.PACKAGE))
                .add(new InterfaceMetaInfo("2", "第二级1", "1", 2, false, InterfaceMetaType.CONTROLLER))
                .add(new InterfaceMetaInfo("3", "第二级2", "1", 2, true, InterfaceMetaType.INTERFACE))
                .add(new InterfaceMetaInfo("4", "第二级3", "1", 2, false, InterfaceMetaType.CONTROLLER))
                .add(new InterfaceMetaInfo("5", "第三级1", "2", 3, true, InterfaceMetaType.INTERFACE))
                .add(new InterfaceMetaInfo("6", "第三级2", "4", 4, true, InterfaceMetaType.INTERFACE))
                .build();

    }

    public TreeModel loadTree(Module module){
        final List<PsiJavaFile> javaFiles = getValidFiles(module).stream()
                .map(f -> ((PsiJavaFile) PsiUtilCore.getPsiFile(module.getProject(), f)))
                .collect(Collectors.toList());

        final Map<PsiJavaFile, PsiClass> classList = javaFiles.stream().collect(Collectors.toMap(f -> f, f -> reader.getMainClass(f)));
        final List<InterfaceMetaInfo> controllerList = classList.entrySet().stream().map(e -> new InterfaceMetaInfo(e.getKey(), reader, e.getValue())).collect(Collectors.toList());
        final List<InterfaceMetaInfo> interfaceList = classList.entrySet().stream().flatMap(e -> createInterface(e.getValue())).collect(Collectors.toList());
        final List<InterfaceMetaInfo> packageList = new ArrayList<>();
        javaFiles.stream().collect(Collectors.groupingBy(f -> f.getPackageName()))
                .forEach((key, list) -> packageList.add(new InterfaceMetaInfo(key, list, reader)));

        List<InterfaceMetaInfo> list = new ArrayList<>(controllerList.size() + interfaceList.size() + packageList.size());
        list.addAll(packageList);
        list.addAll(controllerList);
        list.addAll(interfaceList);
        return InterfaceMetaInfo.buildTree(list, module);
    }

    private Stream<InterfaceMetaInfo> createInterface(PsiClass javaClass) {
        return Arrays.stream(javaClass.getMethods()).filter(m -> reader.isValidInterface(m))
                .map(m -> new InterfaceMetaInfo(m, javaClass, reader));
    }

    private DefaultMutableTreeNode createRootNode(Module module) {
        return null;
    }

    private List<VirtualFile> getValidFiles(Module module){
        final VirtualFile[] sourceRoots = ModuleRootManager.getInstance(module).getSourceRoots(false);
        sourceRoots[0].getFileType();
        return Arrays.stream(sourceRoots)
                .flatMap(root -> VfsUtil.collectChildrenRecursively(root).stream())
                .filter(file -> {
                    if (file.isDirectory()) {
                        return false;
                    } else if (!file.getFileType().getDefaultExtension().equalsIgnoreCase("java")) {
                        return false;
                    } else {
                        try {
                            final String loadText = VfsUtilCore.loadText(file);
                            return loadText.contains("@RestController")
                                    || loadText.contains("@Controller");
                        } catch (IOException e) {
                            log.error("读取文件失败", e);
                            return false;
                        }
                    }
                })
                .collect(Collectors.toList());
    }

    private boolean isValidModule(Module module) {
        final ModuleRootManager moduleRootManager = ModuleRootManager.getInstance(module);
        final Facet<?>[] allFacets = FacetManager.getInstance(module).getAllFacets();
        return Objects.nonNull(moduleRootManager.getSdk())
                && moduleRootManager.getSdk().getSdkType().getName().contains("Java")
                && Arrays.stream(allFacets).anyMatch(f -> f.getName().contains("Spring"));
    }

    public String getErrorMsg(int status) {
        return ERROR_MSG.get(String.valueOf(status));
    }

    public List<Module> getValidModules(Project project) {
        return Arrays.stream(ModuleManager.getInstance(project).getModules())
                .filter(this::isValidModule)
                .collect(Collectors.toList());
    }
}
