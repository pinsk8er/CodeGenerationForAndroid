package pin.hobby.cga.util;

/**
 * Created by pin-mint on 16. 4. 13.
 */
public enum CCGAType {

        CGAType_Packge(0),
        CGAType_Imports(1),
        CGAType_ClassName(2),
        CGAType_MemberVariable(3),
        CGAType_LayoutId(4),
        CGAType_FindView(5),
        CGAType_FindView_2(6),
        CGAType_TargetClass(7);

        private int idx;
        private String str;
        CCGAType(int i) {
            idx = i;
            switch (i)
            {
                case 0:
                    str = "\\$PACKAGE";
                    break;
                case 1:
                    str = "\\$IMPORTS";
                    break;
                case 2:
                    str = "\\$CLASSNAME";
                    break;
                case 3:
                    str = "\\$MEMBER_VARIABLE";
                    break;
                case 4:
                    str = "\\$LAYOUT_ID";
                    break;
                case 5:
                    str = "\\$FIND_VIEW";
                    break;
                case 6:
                    str = "\\$FIND_VIEW\\(([0-9a-zA-Z]+)\\)";
                    break;
                case 7:
                    str = "\\$TAGET_CLASSNAME";
                    break;
                default:
                    str = "";
                    break;
            }
        }

        public int getIdx()
        {
            return idx;
        }

        public String getPattern()
        {
            return str;
        }


    public static final int TypeSize = 8;

    static public CCGAType getType(int idx)
    {

        switch (idx)
        {
            case 0:
                return CCGAType.CGAType_Packge;
            case 1:
                return CCGAType.CGAType_Imports;
            case 2:
                return CCGAType.CGAType_ClassName;
            case 3:
                return CCGAType.CGAType_MemberVariable;
            case 4:
                return CCGAType.CGAType_LayoutId;
            case 5:
                return CCGAType.CGAType_FindView;
            case 6:
                return CCGAType.CGAType_FindView_2;
            case 7:
                return CCGAType.CGAType_TargetClass;
            default:
                return null;
        }
    }
}
