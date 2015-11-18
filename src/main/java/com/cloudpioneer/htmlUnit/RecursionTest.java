package com.cloudpioneer.htmlUnit;

/**
 * 利用递归实现，将输入的一个字符串中的所有元素进行排序并输出
 */
public class RecursionTest {

    public static void permute(char[] list, int low, int high) {
        int i;
        if (low == high) {
            String cout = "";
            for (i = 0; i <= high; i++) {
                cout += list[i];
            }
            System.out.println(cout);
        } else {
            for (i = low; i <= high; i++) {
                char temp = list[low];
                list[low] = list[i];
                list[i] = temp;
                permute(list, low + 1, high);
                temp = list[low];
                list[low] = list[i];
                list[i] = temp;
            }
        }
    }

    public static void main(String[] args) {
        String str = "abc";
        char[] strArray = str.toCharArray();
        permute(strArray, 0, strArray.length - 1);
    }
}