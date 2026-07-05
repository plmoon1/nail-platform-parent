package com.nail.common.util;

import cn.hutool.crypto.digest.BCrypt;

/**
 * 密码加密校验工具类
 * 使用BCrypt算法进行密码加密和校验
 *
 * @author nail-platform
 */
public class PasswordUtil {

    /**
     * 加密密码
     *
     * @param password 明文密码
     * @return 加密后的密码
     */
    public static String encrypt(String password) {
        if (password == null || password.isEmpty()) {
            throw new IllegalArgumentException("密码不能为空");
        }
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    /**
     * 校验密码
     *
     * @param password       明文密码
     * @param hashedPassword 加密后的密码
     * @return 是否匹配
     */
    public static boolean matches(String password, String hashedPassword) {
        if (password == null || hashedPassword == null) {
            return false;
        }
        try {
            return BCrypt.checkpw(password, hashedPassword);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 生成随机密码（8位字母数字组合）
     *
     * @return 随机密码
     */
    public static String generateRandomPassword() {
        return generateRandomPassword(8);
    }

    /**
     * 生成随机密码
     *
     * @param length 密码长度
     * @return 随机密码
     */
    public static String generateRandomPassword(int length) {
        if (length < 4) {
            throw new IllegalArgumentException("密码长度不能小于4");
        }

        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder password = new StringBuilder();

        for (int i = 0; i < length; i++) {
            int index = (int) (Math.random() * chars.length());
            password.append(chars.charAt(index));
        }

        return password.toString();
    }

    /**
     * 校验密码强度
     *
     * @param password 密码
     * @return 强度等级（1-弱，2-中，3-强）
     */
    public static int checkStrength(String password) {
        if (password == null || password.isEmpty()) {
            return 0;
        }

        int strength = 0;

        // 长度检查
        if (password.length() >= 8) {
            strength++;
        }

        // 包含小写字母
        if (password.matches(".*[a-z].*")) {
            strength++;
        }

        // 包含大写字母
        if (password.matches(".*[A-Z].*")) {
            strength++;
        }

        // 包含数字
        if (password.matches(".*\\d.*")) {
            strength++;
        }

        // 包含特殊字符
        if (password.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?].*")) {
            strength++;
        }

        // 归一化为1-3级
        if (strength <= 2) {
            return 1; // 弱
        } else if (strength <= 4) {
            return 2; // 中
        } else {
            return 3; // 强
        }
    }

    /**
     * 校验密码是否符合基本要求
     * 要求：长度8-20位，包含字母和数字
     *
     * @param password 密码
     * @return 是否符合要求
     */
    public static boolean isValid(String password) {
        if (password == null || password.isEmpty()) {
            return false;
        }

        // 长度检查
        if (password.length() < 8 || password.length() > 20) {
            return false;
        }

        // 包含字母
        boolean hasLetter = password.matches(".*[a-zA-Z].*");
        // 包含数字
        boolean hasDigit = password.matches(".*\\d.*");

        return hasLetter && hasDigit;
    }

    /**
     * 获取密码强度描述
     *
     * @param password 密码
     * @return 强度描述
     */
    public static String getStrengthDescription(String password) {
        int strength = checkStrength(password);
        switch (strength) {
            case 0:
                return "无效";
            case 1:
                return "弱";
            case 2:
                return "中";
            case 3:
                return "强";
            default:
                return "未知";
        }
    }
}
