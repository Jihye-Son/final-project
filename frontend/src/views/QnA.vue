<template>
<v-container >
    <v-row justify="center" class="mt-1">
        <v-col xs="12" sm="12" md="12" lg="9" xl="9">
            <div class="text-h3">{{pagename}}</div>
            <v-row justify="center">
                <v-col cols="auto">
                    <v-btn :to="'/qna/productQnA'">상품문의</v-btn>
                </v-col>
                <v-col cols="auto">
                    <v-btn :to="'/qna/deliveryQnA'">배송문의</v-btn>
                </v-col>
                <v-col cols="auto">
                    <v-btn :to="'/qna/beforeDeliveryQnA'">배송 전 변경/취소</v-btn>
                </v-col>
                <v-col cols="auto">
                    <v-btn :to="'/qna/afterDeliveryQnA'">배송 후 교환/반품</v-btn>
                </v-col>
            </v-row>
            <router-view class="mt-5"></router-view>
        </v-col>
    </v-row>
</v-container>
</template>

<script>
export default {
    data() {
        return {
            pagename: '',
        }
    },
    methods: {
        currentURL() {
            let link = this.$route.path;
            link = link.slice(5, link.length);
            let pageList = [{
                text: '상품문의',
                value: 'productQnA',
            }, {
                text: '배송문의',
                value: 'deliveryQnA',
            }, {
                text: '배송 전 변경/취소',
                value: 'beforeDeliveryQnA',
            }, {
                text: '배송 후 교환/반품',
                value: 'afterDeliveryQnA',
            }, ]
            for (let i = 0; i < 4; i++) {
                if (link == pageList[i].value) {
                    this.pagename = pageList[i].text;
                }
            }
        },
    },
    computed: {},
    watch: {
        '$route'() {
            this.currentURL();
        }
    },
    mounted() {
        this.currentURL();
    }
}
</script>

<style>

</style>
