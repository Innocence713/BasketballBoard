# BasketballBoard
##篮球战术画板

        体育学院的一个老师做一个创新教学的课题，找我们实验室的老师做一个篮球战术画板，由于我们实验室主要是做硬件嵌入式的，所以
        
    老师就让我和另一个同学用DE1-SOC-MTL2开发平台的FPGA做。我们用基于FPGA的Nios II软核搭一个CPU，然后用C语言写裸机程序。
    
    由没有操作系统、Nios II 软核实现的CPU的性能也非常差，而且用C语言写起来也非常复杂，所以我们只实现了非常简单的功能。过年
    
    来了后另一个同学复习考研了，所以我就打算用Android写一个App, 在这之前我写过一些非常简单的小程序，如wifi小车App和视频
    
    监控App等，而且自己也对Android开发比较感兴趣，平时有空时也看了一些Android知识，也想今后往这方面发展，自己也比较有热情，
    
    整个过程也还算比较顺利。
    
        里面的资源图片是找的别人的App里的解压出来的，整个框架只是用的AndroidStudio自带的Navigation Drawer Activity, 然后去
    
    掉了Toolbar等，自己重新添加了TitleBar，侧边栏也做了一些修改。
  
  
   
###功能：
- 1. 分段画战术轨迹，最后合成播放
- 2. 多次重复播放
- 3. 改变轨迹颜色和宽度
- 4. 战术保存
- 5. 战术库读取播放，删除战术



--------
###演示gif
![](https://github.com/Innocence713/BasketballBoard/blob/master/GifDemo/1.gif)     					 ![](https://github.com/Innocence713/BasketballBoard/blob/master/GifDemo/2.gif)    	![](https://github.com/Innocence713/BasketballBoard/blob/master/GifDemo/3.gif)    
 
