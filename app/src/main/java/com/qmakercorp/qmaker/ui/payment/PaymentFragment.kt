package com.qmakercorp.qmaker.ui.payment

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast

import com.qmakercorp.qmaker.R

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import br.com.uol.pslibs.checkout_in_app.PSCheckout
import br.com.uol.pslibs.checkout_in_app.transparent.listener.PSBilletListener
import br.com.uol.pslibs.checkout_in_app.transparent.listener.PSCheckoutListener
import br.com.uol.pslibs.checkout_in_app.transparent.listener.PSInstallmentsListener
import br.com.uol.pslibs.checkout_in_app.transparent.vo.InstallmentVO
import br.com.uol.pslibs.checkout_in_app.transparent.vo.PSBilletRequest
import br.com.uol.pslibs.checkout_in_app.transparent.vo.PSCheckoutResponse
import br.com.uol.pslibs.checkout_in_app.transparent.vo.PSInstallmentsResponse
import br.com.uol.pslibs.checkout_in_app.transparent.vo.PSTransparentDefaultRequest
import br.com.uol.pslibs.checkout_in_app.transparent.vo.PaymentResponseVO
import br.com.uol.pslibs.checkout_in_app.wallet.listener.MainCardCallback
import br.com.uol.pslibs.checkout_in_app.wallet.util.PSCheckoutConfig
import br.com.uol.pslibs.checkout_in_app.wallet.view.components.PaymentButton
import br.com.uol.pslibs.checkout_in_app.wallet.vo.PSCheckoutRequest
import br.com.uol.pslibs.checkout_in_app.wallet.vo.PSWalletMainCardVO
import br.com.uol.pslibs.checkout_in_app.wallet.vo.PagSeguroResponse
import butterknife.BindView
import butterknife.BuildConfig
import butterknife.ButterKnife
import butterknife.OnClick
import kotlinx.android.synthetic.main.payment_fragment.*

class PaymentFragment : Fragment() {

    private val SELLER_EMAIL = "thomas.marquesbr@gmail.com"
    private val SELLER_TOKEN = "BC803E00726E4441A610B48F1554663E"
    private val SELLER_TOKEN_DEBUG = "0114A1ED833A4282AAAE723F318BEA26"
//    private val NOTIFICATION_URL_PAYMENT = "https://pagseguro.uol.com.br/lojamodelo-qa/RetornoAutomatico-OK.jsp"
    private val NOTIFICATION_URL_PAYMENT_DEBUG = "https://ws.sandbox.pagseguro.uol.com.br/v2/checkout"

    private var psCheckoutListener = object : br.com.uol.pslibs.checkout_in_app.wallet.listener.PSCheckoutListener {
        override fun onSuccess(pagSeguroResponse: PagSeguroResponse, context: Context) {
            Toast.makeText(activity, "Sucesso de pagamento", Toast.LENGTH_LONG).show()
        }
        override fun onFailure(pagSeguroResponse: PagSeguroResponse, context: Context) {
            Toast.makeText(activity, "Falha no pagamento", Toast.LENGTH_LONG).show()
        }
        override fun onProgress(context: Context) {
            Toast.makeText(activity, "Pagamento em andamento", Toast.LENGTH_LONG).show()
        }
        override fun onCloseProgress(context: Context) {}
    }

    internal var psInstallmentsListener: PSInstallmentsListener = object : PSInstallmentsListener {
        override fun onSuccess(responseVO: PSInstallmentsResponse) {
            //responseVO objeto com a lista de parcelas
            // Item da lista de parcelas InstallmentVO:
            // (String) installmentVO.getCardBrand() - Bandeira do cartão;
            // (int) - installmentVO.getQuantity() - quantidade da parcela;
            // (Double) - installmentVO.getAmount() - valor da parcela;
            // (Double) - installmentVO.getTotalAmount() - Valor total da transação parcelada;
            val installmentVO = responseVO.installments[responseVO.installments.size - 1]
            Toast.makeText(activity, "Parcelado em " +
                    installmentVO.quantity + " x R$" + installmentVO.amount, Toast.LENGTH_LONG).show()
        }

        override fun onFailure(message: String) {
            // falha na requisicao
            Toast.makeText(activity, message, Toast.LENGTH_LONG).show()
        }
    }

    private val psCheckoutListenerTransparent = object : PSCheckoutListener {
        override fun onSuccess(responseVO: PSCheckoutResponse) {
            // responseVO.getCode() - Codigo da transação
            // responseVO.getStatus() - Status da transação
            // responseVO.getMessage() - Mensagem de retorno da transação(Sucesso/falha)
            Toast.makeText(activity, "Success: " + responseVO.message, Toast.LENGTH_LONG).show()
        }

        override fun onFailure(responseVO: PSCheckoutResponse) {
            Toast.makeText(activity, "Fail: " + responseVO.message, Toast.LENGTH_LONG).show()
        }

        override fun onProcessing() {
            Toast.makeText(activity, "Processing...", Toast.LENGTH_LONG).show()
        }
    }

    private val psBilletListener = object : PSBilletListener {
        override fun onSuccess(responseVO: PaymentResponseVO) {
            // responseVO.getBookletNumber() - numero do codigo de barras do boleto
            // responseVO.getPaymentLink() - link para download do boleto
            Toast.makeText(activity, "Gerou boleto com o numero: " + responseVO.bookletNumber, Toast.LENGTH_LONG).show()
        }

        override fun onFailure(e: Exception) {
            // Error
            Toast.makeText(activity, "Falha ao gerar boleto", Toast.LENGTH_LONG).show()
        }

        override fun onProcessing() {
            // Progress
            Toast.makeText(activity, "Processing...", Toast.LENGTH_LONG).show()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.payment_fragment, container, false)
        ButterKnife.bind(this, view)

        initWallet()
        initTransparent()
        configurePayment()
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        credit_card_default.setOnClickListener { paymentCreditCardDefault() }
    }

    private fun initWallet() {
        //Inicialização a lib com parametros necessarios
        val psCheckoutConfig = PSCheckoutConfig()
        psCheckoutConfig.sellerEmail = SELLER_EMAIL
        psCheckoutConfig.sellerToken = if (com.qmakercorp.qmaker.BuildConfig.DEBUG)
            SELLER_TOKEN_DEBUG
        else
            SELLER_TOKEN
        //Informe o fragment container
        psCheckoutConfig.container = R.id.fragment_container

        //Inicializa apenas os recursos da carteira
        PSCheckout.initWallet(activity!!, psCheckoutConfig)
    }

    private fun initTransparent() {
        val psCheckoutConfig = PSCheckoutConfig()
        psCheckoutConfig.sellerEmail = SELLER_EMAIL
        psCheckoutConfig.sellerToken = if (com.qmakercorp.qmaker.BuildConfig.DEBUG)
            SELLER_TOKEN_DEBUG
        else
            SELLER_TOKEN
        //Informe o fragment container
        psCheckoutConfig.container = R.id.fragment_container

        //Inicializa apenas os recursos de pagamento transparente e boleto
        PSCheckout.initTransparent(activity!!, psCheckoutConfig)
    }

    fun configurePayment() {
        val productId = "001"
        val description = "CAFE NESPRESSO"

//                cardWallet.configurePayment(productId, description, 2.50, 1, R.id.fragment_container, getActivity(),
//                        SELLER_EMAIL, SELLER_TOKEN, psCheckoutListener);
    }

//    fun listCards(view: View) {
//        Toast.makeText(activity, "Listar cartões", Toast.LENGTH_LONG).show()
//        PSCheckout.showListCards()
//    }
//
//    fun getMainCard(view: View) {
//        Toast.makeText(activity, "Pegar o cartão principal", Toast.LENGTH_LONG).show()
//
//        //String getCardBrand() -> Retorna o nome da bandeira do cartão principal;
//        //String getFinalCard() -> Retorna o final do cartão utilizado
//
//        //Exemplo utilização do metodo PSCheckout.getMainCard(mainCardCallback)
//
//        /* Obtem o cartão principal setado na lib, caso queira deixar visivel no App.
//        *
//        * @Return String Nome da bandeira do cartão
//        * @Return String Final cartão
//        */
//        if (PSCheckout.isLoggedUser()) {
//            PSCheckout.getMainCard(object : MainCardCallback {
//                override fun onSuccess(mainCardVO: PSWalletMainCardVO?) {
//                    if (mainCardVO != null) {
//                        Toast.makeText(activity, mainCardVO.cardBrand + " **** " + mainCardVO.finalCard, Toast.LENGTH_LONG).show()
//                    } else {
//                        Toast.makeText(activity, "Sem cartão principal no momento...", Toast.LENGTH_LONG).show()
//                    }
//                }
//
//                override fun onFail() {
//
//                }
//            })
//        } else {
//            Toast.makeText(activity, "Usuario não está logado", Toast.LENGTH_LONG).show()
//        }
//    }
//
//    fun paymentCreditCardWallet(view: View) {
//        Toast.makeText(activity, "Pagamento com cartão de credito carteira", Toast.LENGTH_LONG).show()
//        //Valor do produto / serviço
//        val productPrice = 1.0
//        //id do produto
//        val productId = "001"
//        //Descrição do produto
//        val description = "Produto Exemplo"
//
//        val psCheckoutRequest = PSCheckoutRequest().withReferenceCode("123")
//                .withNewItem(description, "1", productPrice, productId)
//
//        PSCheckout.payWallet(psCheckoutRequest, psCheckoutListener)
//    }
//
//    fun logout(view: View) {
//        Toast.makeText(activity, "Logout do carteira", Toast.LENGTH_LONG).show()
//        PSCheckout.logout(activity!!)
//    }
//
//    fun installment(view: View) {
//        Toast.makeText(activity, "Parcelamento do pagamento", Toast.LENGTH_LONG).show()
//        PSCheckout.getInstallments("4024007192262755", "50.00", psInstallmentsListener)
//    }

    fun paymentCreditCardDefault() {
        val installment = InstallmentVO()
        installment.amount = java.lang.Double.parseDouble("5.00")
        installment.quantity = 1


        var notification_url_payment = ""

        if (BuildConfig.DEBUG)
            notification_url_payment = NOTIFICATION_URL_PAYMENT_DEBUG

        val psTransparentDefaultRequest = PSTransparentDefaultRequest()
        psTransparentDefaultRequest
                .setDocumentNumber("80525612050")
                .setName("João da Silva")
                .setEmail("joao.silva@teste.com")
                .setAreaCode("34")
                .setPhoneNumber("999508523")
                .setStreet("Rua Tapajos")
                .setAddressComplement("")
                .setAddressNumber("23")
                .setDistrict("Saraiva")
                .setCity("Uberlândia")
                .setState("MG")
                .setCountry("BRA")
                .setPostalCode("38408414")
                .setTotalValue("45.00")
                .setAmount("50.00")
                .setQuantity(1)
                .setCreditCardName("João da Silva")
                .setCreditCard("4532645179108668")
                .setCvv("817")
                .setExpMonth("12")
                .setExpYear("18")
                .setDescriptionPayment("Pagamento do teste de integração")
                .setBirthDate("04/05/1988")
                .setExtraAmount("-5.00")
                .setInstallments(null).nottificationUrl = notification_url_payment
        Toast.makeText(activity, "Pagamento com o cartao de credito default", Toast.LENGTH_LONG).show()
        PSCheckout.payTransparentDefault(psTransparentDefaultRequest, psCheckoutListenerTransparent,
                (activity as AppCompatActivity?)!!)
    }

//    fun paymentBillet(view: View) {
//        val psBilletRequest = PSBilletRequest()
//        psBilletRequest
//                .setDocumentNumber("99404021040") // Documento do comprador
//                .setName("João da silva") // Nome do comprador
//                .setEmail("joao.silva@teste.com") // Email do comprador
//                .setAreaCode("34") // Código de área (ddd)
//                .setPhoneNumber("999508523") //Numero do telefone sem o ddd
//                .setStreet("Rua Tapajos") // Rua do comprador
//                .setAddressComplement("") // Complemento do endereco
//                .setAddressNumber("23") // Numero do endereco
//                .setDistrict("Saraiva") // Bairro
//                .setCity("Uberlândia") //Cidade
//                .setState("MG") //Estado
//                .setCountry("Brasil") // País
//                .setPostalCode("38408414") //CEP
//                .setTotalValue(2.50) //VALOR DA TRANSACAO
//                .setAmount(2.50) // MONTANTE DA TRANSACAO
//                .setDescriptionPayment("Pagamento do teste de integração") //DESCRICAO DO PRODUTO/SERVICO
//                .setExtraAmount("-0.50")
//                .setQuantity(1).nottificationUrl = NOTIFICATION_URL_PAYMENT
//
//        Toast.makeText(activity, "Pagamento com o boleto", Toast.LENGTH_LONG).show()
//        PSCheckout.generateBooklet(psBilletRequest, psBilletListener, (activity as AppCompatActivity?)!!)
//    }

}
