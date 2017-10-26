package com.luolei.pattern.composite.ch1;

/**
 * 欲开发一个杀毒(AntiVirus)软件，该软件既可以对某个文件夹(Folder)杀毒，也可以对某个指定的文件(File)进行杀毒。
 * 该杀毒软件还可以根据各类文件的特点，为不同类型的文件提供不同的杀毒方式，例如图像文件(ImageFile)和文本文件(TextFile)的杀毒方式就有所差异。
 * 现需要提供该杀毒软件的整体框架设计方案。
 *
 * 在文件夹中可以包含文件，还可以继续包含子文件夹，但是在文件中不能再包含子文件或者子文件夹。
 * 在此，我们可以称文件夹为容器(Container)，而不同类型的各种文件是其成员，也称为叶子(Leaf)，一个文件夹也可以作为另一个更大的文件夹的成员。
 * 如果我们现在要对某一个文件夹进行操作，如查找文件，那么需要对指定的文件夹进行遍历，如果存在子文件夹则打开其子文件夹继续遍历，如果是文件则判断之后返回查找结果。
 *
 *
 * @author luolei
 * @date 2017-03-30 10:55
 */
public class Client {
    public static void main(String args[]) {
        Folder folder1, folder2, folder3;
        folder1 = new Folder("Sunny的资料");
        folder2 = new Folder("图像文件");
        folder3 = new Folder("文本文件");

        ImageFile image1, image2;
        image1 = new ImageFile("小龙女.jpg");
        image2 = new ImageFile("张无忌.gif");

        TextFile text1, text2;
        text1 = new TextFile("九阴真经.txt");
        text2 = new TextFile("葵花宝典.doc");

        folder2.addImageFile(image1);
        folder2.addImageFile(image2);
        folder3.addTextFile(text1);
        folder3.addTextFile(text2);
        folder1.addFolder(folder2);
        folder1.addFolder(folder3);

        folder1.killVirus();
    }
}
