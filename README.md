
Install
=======
1. Settings > Plugins
![](ReadmeRes/install.png)

1. Select File
![](ReadmeRes/select_file.png)

1. Restart Android Studio

Settings
========
1. Code Setting
![](ReadmeRes/setting_1.png)

1. Add Type
    * Type Name : key (can not overlap)
    * Calss Last Name : Default Class Last Name 
        ex> Last Name is 'Activity' -> XXXXActivity
    * File Type : select file type.    
      ![](ReadmeRes/setting_2.png)


TAG INFO 
========
* Common File Type 
    >+ $PACKAGE : pakage name 
        ex> com.xx.xx
        
    >+ $CLASSNAME : class name

* Select File Type 'XML'

    >+ $IMPORTS : import list
        ex> 
        import android.support.v7.widget.RecyclerView;
        import android.view.View;
        import android.widget.ImageView;
        .....

    >+ $MEMBER_VARIABLE : member variable
        ex> 
        private RecyclerView        rcvList;
        private ImageView           ivImg;
        .....

    >+ $LAYOUT_ID : layout id
        ex> R.layout.xxxx
        
    >+ $FIND_VIEW : find view source
        ex> 
        txTxt = (TextView)findViewById(R.id.tvText);
        ivImg = (ImageView)findViewById(R.id.ivImg);
        ....
        
    >+ $FIND_VIEW(+) : find view source.   
        ex> $FIND_VIEW(layoutView)
        txTxt = (TextView)layoutView.findViewById(R.id.tvText);
        ivImg = (ImageView)layoutView.findViewById(R.id.ivImg);
        ....
        

* Select File Type 'JAVA'
    >+ $TAGET_CLASSNAME : Select File Class Name


Excute
======

* Action (Xml or Java)
    + Alt + Insert  
        ![](ReadmeRes/action_1.png)
    + Mouse left button click   
        ![](ReadmeRes/action_2.png) => ![](ReadmeRes/action_1.png)

* Make File Dialog  
    ![](ReadmeRes/make_code.png)
    + Select package  
        ![](ReadmeRes/select_package.png)
