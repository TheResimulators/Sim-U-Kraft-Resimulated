package com.resimulators.simukraft.structure;

import com.google.common.collect.Maps;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.datafix.FixTypes;
import net.minecraft.world.gen.structure.template.Template;
import org.apache.commons.io.IOUtils;

import javax.annotation.Nullable;
import java.io.*;
import java.util.Map;

/**
 * Created by fabbe on 2019-02-12 - 9:02 AM.
 */
public class TemplateManagerPlus {
    private final Map<String, TemplatePlus> templates = Maps.newHashMap();
    /**
     * the folder in the assets folder where the structure templates are found.
     */
    private final String baseFolder;
    private final DataFixer fixer;

    public TemplateManagerPlus(String baseFolder, DataFixer dataFixer) {
        this.baseFolder = baseFolder;
        this.fixer = dataFixer;
    }

    public TemplatePlus getTemplate(@Nullable MinecraftServer server, ResourceLocation id) {
        TemplatePlus template = this.get(server, id);

        if (template == null) {
            template = new TemplatePlus();
            this.templates.put(id.getPath(), template);
        }

        return template;
    }

    @Nullable
    public TemplatePlus get(@Nullable MinecraftServer server, ResourceLocation templatePath) {
        String s = templatePath.getPath();

        if (this.templates.containsKey(s)) {
            return this.templates.get(s);
        } else {
            if (server == null) {
                this.readTemplateFromJar(templatePath);
            } else {
                this.readTemplate(templatePath);
            }

            return this.templates.getOrDefault(s, null);
        }
    }

    /**
     * This reads a structure template from the given location and stores it.
     * This first attempts get the template from an external folder.
     * If it isn't there then it attempts to take it from the minecraft jar.
     */
    public boolean readTemplate(ResourceLocation server) {
        String s = server.getPath();
        File file1 = new File(this.baseFolder, s + ".nbt");

        if (!file1.exists()) {
            return this.readTemplateFromJar(server);
        } else {
            InputStream inputstream = null;

            try {
                inputstream = new FileInputStream(file1);
                this.readTemplateFromStream(s, inputstream);
                return true;
            } catch (Throwable var10) {} finally {
                IOUtils.closeQuietly(inputstream);
            }

            return false;
        }
    }

    /**
     * reads a template from the minecraft jar
     */
    private boolean readTemplateFromJar(ResourceLocation id) {
        String s = id.getNamespace();
        String s1 = id.getPath();
        InputStream inputstream = null;

        try {
            inputstream = MinecraftServer.class.getResourceAsStream("/assets/" + s + "/structures/" + s1 + ".nbt");
            this.readTemplateFromStream(s1, inputstream);
            return true;
        } catch (Throwable var10) {} finally {
            IOUtils.closeQuietly(inputstream);
        }

        return false;
    }

    /**
     * reads a template from an inputstream
     */
    private void readTemplateFromStream(String id, InputStream stream) throws IOException {
        NBTTagCompound nbttagcompound = CompressedStreamTools.readCompressed(stream);

        if (!nbttagcompound.hasKey("DataVersion", 99)) {
            nbttagcompound.setInteger("DataVersion", 500);
        }

        TemplatePlus template = new TemplatePlus();
        template.read(this.fixer.process(FixTypes.STRUCTURE, nbttagcompound));

        this.templates.put(id, template);
    }

    /**
     * writes the template to an external folder
     */
    public boolean writeTemplate(@Nullable MinecraftServer server, ResourceLocation id, NBTTagCompound additionalParameters) {
        String s = id.getPath();

        if (server != null && this.templates.containsKey(s)) {
            File file1 = new File(this.baseFolder);

            if (!file1.exists()) {
                if (!file1.mkdirs()) {
                    return false;
                }
            } else if (!file1.isDirectory()) {
                return false;
            }

            File file2 = new File(file1, s + ".nbt");
            TemplatePlus template = this.templates.get(s);
            OutputStream outputstream = null;

            try {
                NBTTagCompound nbttagcompound = template.writeToNBT(additionalParameters);
                outputstream = new FileOutputStream(file2);
                CompressedStreamTools.writeCompressed(nbttagcompound, outputstream);
                return true;
            } catch (Throwable var13) {} finally {
                IOUtils.closeQuietly(outputstream);
            }

            return false;
        } else {
            return false;
        }
    }

    public void remove(ResourceLocation templatePath) {
        this.templates.remove(templatePath.getPath());
    }
}
