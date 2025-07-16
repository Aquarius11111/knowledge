package com.example.user.config;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.UUID;

@Configuration
public class JwtKeyConfig {

    @Value("${jwt.secret-path}")
    private String secretPath;

    @Value("${jwt.old-secret-path}")
    private String oldSecretPath;

    @Getter
    private String currentSecret;

    @Getter
    private String oldSecret;

    @PostConstruct
    public void initSecret() throws IOException {
        loadSecrets();
    }

    private void loadSecrets() throws IOException {
        File currentFile = new File(secretPath);
        File oldFile = new File(oldSecretPath);

        if (!currentFile.exists()) {
            currentSecret = generateAndSaveSecret(currentFile);
        } else {
            currentSecret = Files.readString(currentFile.toPath());
        }

        if (oldFile.exists()) {
            oldSecret = Files.readString(oldFile.toPath());
        }
    }

    // 定期更新秘钥（单位毫秒）
    @Scheduled(fixedRateString = "${jwt.refresh-interval-minutes}000")
    public void rotateSecret() throws IOException {
        // 将旧秘钥保存
        Files.writeString(new File(oldSecretPath).toPath(), currentSecret);

        // 生成新秘钥
        currentSecret = generateAndSaveSecret(new File(secretPath));
        System.out.println("🔐 JWT密钥已自动更新！");
        loadSecrets();//更新secrets
    }

    private String generateAndSaveSecret(File file) throws IOException {
        String newSecret = UUID.randomUUID().toString().replace("-", "") + System.currentTimeMillis();
        Files.createDirectories(file.getParentFile().toPath());
        Files.writeString(file.toPath(), newSecret);
        return newSecret;
    }
}
