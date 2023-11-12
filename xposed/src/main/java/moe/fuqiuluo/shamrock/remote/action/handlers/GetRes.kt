package moe.fuqiuluo.shamrock.remote.action.handlers

import android.util.Base64
import kotlinx.serialization.json.JsonElement
import moe.fuqiuluo.shamrock.remote.action.ActionSession
import moe.fuqiuluo.shamrock.remote.action.IActionHandler
import moe.fuqiuluo.shamrock.remote.service.data.OutFile
import moe.fuqiuluo.shamrock.tools.EmptyJsonString
import moe.fuqiuluo.shamrock.utils.FileUtils

internal object GetRes: IActionHandler() {
    override suspend fun internalHandle(session: ActionSession): String {
        val resId = session.getString("res_id")
            .replace(regex = "[{}\\-]".toRegex(), replacement = "")
            .replace(" ", "")
            .split(".")[0].lowercase()
        return invoke(resId, session.echo)
    }

    operator fun invoke(resId: String, echo: JsonElement = EmptyJsonString): String {
        val file = FileUtils.getFile(resId)

        return if (file.exists()) {
            ok(OutFile(Base64.encodeToString(file.readBytes(), Base64.NO_WRAP)), echo)
        } else {
            error("not found file from cache", echo)
        }
    }

    override val requiredParams: Array<String> = arrayOf("res_id")

    override fun path(): String = "get_res"
}