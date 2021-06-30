package net.wdsj.mcserver.gui.common.component.page

import com.meowray.common.page.PageMeow
import com.meowray.common.page.PageQuery
import net.wdsj.mcserver.gui.common.gui.menu.GuiMenuStatic

/**
 * @author MeowRay
 * @version 1.0
 * @date 2021/5/17 21:20
 */
abstract class GuiMenuDynamicLoadPage<T, Handler, Item>  : GuiMenuPage<T, Handler, Item>
 {
      private val pageMeow: PageMeow<T>

     constructor(
        guiMenu: GuiMenuStatic<Handler, Item>,  pageQuery: PageQuery<T>, preSlot: Int,
        nextSlot: Int) : super(guiMenu, preSlot, nextSlot) {
         pageMeow = PageMeow(pageQuery, freeSlots.size )
    }

    constructor(  guiMenu: GuiMenuStatic<Handler, Item>,   pageQuery: PageMeow<T>, preSlot: Int,
        nextSlot: Int) :  super(guiMenu,preSlot,nextSlot){
            this.pageMeow = pageQuery
    }

     override fun loadPage(page: Int) {
         pageMeow.getPageLoad(page)
         super.loadPage(page)
     }


/*
    override fun PreClick(): Executor<Handler> {
        return Executor {
            pageMeow.getPageLoad(openPage -1)
         //   pageMeow.prePage()
            super.PreClick().execute(it)
        }
    }

    override fun NextClick(): Executor<Handler> {
        return Executor {
            pageMeow.getPageLoad(openPage +1)
        //    pageMeow.nextPage()
            super.NextClick().execute(it)
        }
    }
*/


    override fun getContent(p0: Int): T? {
        try {
            return pageMeow.getData(p0)
        } catch (t: Throwable) {
        }
        return null
    }

    override fun getContentSize(): Int {
        return if (pageMeow.isEnd()) pageMeow.getDataSize() else pageMeow.getDataSize() + 1
    }

}