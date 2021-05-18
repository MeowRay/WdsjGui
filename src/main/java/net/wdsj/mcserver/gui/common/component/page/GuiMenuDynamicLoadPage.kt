package net.wdsj.mcserver.gui.common.component.page

import com.meowray.common.page.PageMeow
import com.meowray.common.page.PageQuery
import net.wdsj.mcserver.gui.common.gui.menu.GuiMenuStatic
import net.wdsj.servercore.interfaces.Executor

/**
 * @author MeowRay
 * @version 1.0
 * @date 2021/5/17 21:20
 */
abstract class GuiMenuDynamicLoadPage<T, Handler, Item>(
    guiMenu: GuiMenuStatic<Handler, Item>, val pageQuery: PageQuery<T>, preSlot: Int,
    nextSlot: Int
) : GuiMenuPage<T, Handler, Item>(
    guiMenu, preSlot, nextSlot
) {
    val pageMeow = PageMeow(pageQuery, freeSlots.size)

    init {
        pageMeow.nextPage()
    }

    override fun PreClick(): Executor<Handler> {
        return Executor {
            pageMeow.prePage()
            super.PreClick().execute(it)
        }
    }

    override fun NextClick(): Executor<Handler> {
        return Executor {
            pageMeow.nextPage()
            super.NextClick().execute(it)
        }
    }


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