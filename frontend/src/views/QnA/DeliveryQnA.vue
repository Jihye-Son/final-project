<template>
<v-container>
    <v-data-table :headers="headers" :options.sync="options" :items="contents" :server-items-length="totalContents" :loading="loading" class="elevation-1" item-key="qnaNo" @click:row="moveto" disable-sort no-data-text="검색된 자료가 없습니다" :footer-props="{'items-per-page-options': [5, 10, 15]}">
        <template #[`item.type`]="{item}">
            <div class="text-left">
                <QnATitleDisplay :type="item.type" />
            </div>
        </template>
        <template #[`item.id`]="{item}">
            <div>
                <HideId :id="item.id" />
            </div>
        </template>
        <template #[`item.regDate`]="{item}">
            <div>
                <DateDisplay :regDate="item.regDate" />
            </div>
        </template>
    </v-data-table>

    <v-row align="center" justify="space-between">
        <v-col cols="8" sm="7" md="6" lg="5" xl="4">
            <v-row>
                <v-col cols="4">
                    <v-select :items="searches" v-model="search" hide-details></v-select>
                </v-col>
                <v-col cols="7">
                    <v-text-field v-model="searchWord" hide-details @keyup.enter="getQnA"></v-text-field>
                </v-col>
                <v-col cols="1" class="mt-3">
                    <v-btn @click="getQnA" color="primary">검색</v-btn>
                </v-col>
            </v-row>
        </v-col>
        <v-col cols="auto" v-if="getLogin != null" class="mt-3">
            <v-btn @click="searchReplyFalse" color="primary" v-if="getLogin.user.authority == 'ROLE_ADMIN'">미답변</v-btn>
            <v-btn :to="'/writePost/deliveryQnA'" color="primary" v-if="getLogin.user.authority == 'ROLE_USER'">글쓰기</v-btn>
        </v-col>
    </v-row>
    <v-dialog v-model="alertDialog" max-width="350">
        <v-alert class="mb-0" :type="alertType">
            {{alertMessage}}
        </v-alert>
    </v-dialog>
</v-container>
</template>

<script>
import axios from 'axios'
import HideId from '@/components/HideId.vue'
import DateDisplay from '@/components/DateDisplay.vue'
import QnATitleDisplay from '@/components/QnATitleDisplay.vue'
import {
    createNamespacedHelpers
} from 'vuex'
const LoginStore = createNamespacedHelpers('LoginStore')
export default {
    components: {
        HideId,
        DateDisplay,
        QnATitleDisplay,
    },
    data() {
        return {
            alertDialog: false,
            alertMessage: '',
            alertType: '',
            totalContents: 0,
            contents: [],
            options: {},
            loading: true,
            headers: [{
                    text: '번호',
                    value: 'qnaNo',
                    sortable: false,
                    width: '10%',
                    align: 'center',
                    divider: true,
                },
                {
                    text: '제목',
                    value: 'type',
                    sortable: false,
                    width: '65%',
                    align: 'center',
                    divider: true,
                },
                {
                    text: '작성자',
                    value: 'id',
                    sortable: false,
                    width: '10%',
                    align: 'center',
                    divider: true,
                },
                {
                    text: '작성일',
                    value: 'regDate',
                    sortable: false,
                    width: '15%',
                    align: 'center',
                },
            ],
            searches: [{
                text: '작성자',
                value: 'id'
            }],
            search: 'id',
            searchWord: '',
        }
    },
    methods: {
        getQnA() {
            this.loading = true;
            const {
                page,
                itemsPerPage
            } = this.options
            let link = this.$route.path;
            link = link.slice(5, link.length - 3);
            axios.get(`/api/qna/getQnaListByType`, {
                params: {
                    page: page,
                    perPage: itemsPerPage,
                    search: this.search,
                    searchWord: this.searchWord,
                    type: link
                }
            }).then(res => {
                this.contents = res.data.qnaList;
                this.totalContents = res.data.count;
            }).finally(() => {
                this.loading = false;
            })
        },
        searchQnA() {
            if (this.options.page != 1) {
                this.options.page = 1;
            } else {
                this.getQnA();
            }
        },
        searchReplyFalse() {
            this.search = 'reply';
            this.searchWord = '';
            if (this.options.page != 1) {
                this.options.page = 1;
            } else {
                this.getQnA();
            }
            this.getQnA();
        },
        moveto(item) {
            if (item.secret == true) {
                if (this.getLogin == null || this.getLogin.user.id != item.id && this.getLogin.user.authority != 'ROLE_ADMIN') {
                    if (item.id == 'spring') {
                        axios.get(`/api/qna/getQna/${item.originalNo}`)
                            .then(res => {
                                if (res.data.id == this.getLogin.user.id) {
                                    this.$router.push(`/qna/${item.qnaNo}`)
                                } else {
                                    this.alertDialog = true;
                                    this.alertType = 'warning';
                                    this.alertMessage = '비밀글입니다';
                                }
                            })
                    } else {
                        this.alertDialog = true;
                        this.alertType = 'warning';
                        this.alertMessage = '비밀글입니다';
                    }
                    return;
                }
            }
            this.$router.push(`/qna/${item.qnaNo}`)
        },
    },
    computed: {
        ...LoginStore.mapGetters(['getLogin']),
    },
    watch: {
        options: {
            handler() {
                this.getQnA()
            },
            deep: true,
        },
    },
}
</script>

<style scoped>
</style>
