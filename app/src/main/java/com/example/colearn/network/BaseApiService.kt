package com.example.colearn.network

import android.accounts.NetworkErrorException
import androidx.paging.PageKeyedDataSource
import com.google.gson.JsonSyntaxException
import com.google.gson.stream.MalformedJsonException
import org.apache.http.conn.ConnectTimeoutException
import retrofit2.Response
import java.io.IOException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.net.ssl.SSLException
import javax.net.ssl.SSLHandshakeException

/**
 * Created by Gagan on 27/02/21.
 */

abstract class BaseApiService<T,V> : PageKeyedDataSource<T, V>() {

    protected suspend fun <T : Any> apiCall(call: suspend () -> Response<T>,showErrorMessage:Boolean): Result<T> {
        val response: Response<T>
        try {
            response = call.invoke()
        } catch (t: Throwable) {
            return Result.Error(mapNetworkThrowable(t,showErrorMessage))
        }
        return if (!response.isSuccessful) {
            val errorBody = response.errorBody()
            @Suppress("BlockingMethodInNonBlockingContext")
            Result.Error(parseCustomError(response.message(), response.code(), errorBody?.string() ?: "",showErrorMessage))
        } else {
            return if (response.body() == null) {
                Result.Error(NullPointerException(("response.body() can't be null")))
            } else {
                    if(response.code() == 200){
                        //TODO hardcoded value 10 which needs to be changed as per item per page
                        val lastPage = response?.headers()?.get("x-total")?.toInt()?.div(10)
                        return Result.Success(response.body()!!,lastPage)
                    } else {
                        if(showErrorMessage){
                            showErrorMessage(response.message())
                        }
                       return Result.Error(Exception(response.message()))
                    }
            }
        }
    }

    private fun mapNetworkThrowable(throwable: Throwable, showErrorMessage: Boolean): Exception {
        //HttpException, SocketTimeoutException, etc...

        if(throwable is UnknownHostException) {
           if(showErrorMessage)
               showErrorMessage("No Internet Connection")
            return UnknownHostException(("No Internet Connection"))
        }else if (throwable is SocketTimeoutException) {
            if(showErrorMessage)
                showErrorMessage("Socket Time Out Exception")
            return SocketTimeoutException("Socket Time Out Exception")
        }else if (throwable is ConnectTimeoutException) {
            if(showErrorMessage)
                showErrorMessage("Connection Timed Out")
            return ConnectTimeoutException("Connection Timed Out")
        }else if (throwable is SSLHandshakeException) {
            if(showErrorMessage)
                showErrorMessage("SSL Handshake Error Occurred")
            return SSLHandshakeException("SSL Handshake Error Occurred")
        }else if (throwable is ConnectException) {
            if(showErrorMessage)
                showErrorMessage("Connection Exception")
            return ConnectException("Connection Exception")
        }else if(throwable is SSLException) {
            if(showErrorMessage)
                showErrorMessage("SSL Exception")
            return SSLException("SSL Exception")
        }else if (throwable is JsonSyntaxException || throwable is MalformedJsonException) {
            if(showErrorMessage)
                showErrorMessage("Parsing Error")
            return JsonSyntaxException("Parsing Error")
        }else if (throwable is IOException) {
            if(showErrorMessage)
                showErrorMessage("Something went wrong")
            return IOException("Something went wrong")
        }
        if(showErrorMessage)
            showErrorMessage("Something went wrong")
        return NetworkErrorException("Something went wrong")
    }

    protected fun mapHttpThrowable(throwable: Throwable, code: Int, message: String)/*: HttpBaseException*/ {
        //InvalidGrantException, ForbiddenException, etc...
    }

    protected  fun parseCustomError(responseMessage: String, responseCode: Int, errorBodyJson: String, showErrorMessage: Boolean): Exception {

        when(responseCode){
            411 ->  {
                if(showErrorMessage)
                    showErrorMessage("No Internet Connection")
                return Exception("No Internet Connection")}
            415 ->   {
                if(showErrorMessage)
                showErrorMessage("Api Versioning Error")
                return Exception("Api Versioning Error")}
            else -> {
                if(showErrorMessage)
                    showErrorMessage(responseMessage)
                  return Exception(responseMessage)
            }
        }

    }

    fun showErrorMessage(message: String){

    }
}