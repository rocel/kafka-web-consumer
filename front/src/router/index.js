import Vue from 'vue'
import Router from 'vue-router'
import List from '../components/List'
import Table from '../components/Table'
import Map from '../components/Map'
import Cards from '../components/Cards'
import Pivot from '../components/Pivot'
import PageNotFound from '../components/PageNotFound'

Vue.use(Router)

export default new Router({
    mode: 'history',
    routes: [{
            path: '/table',
            name: 'Table',
            component: Table
        },
        {
            path: '/map',
            name: 'Map',
            component: Map
        },
        {
            path: '/pivot',
            name: 'Pivot',
            component: Pivot
        },
        {
            path: '/cards',
            name: 'Cards',
            component: Cards
        },
        {
            path: '/list',
            name: 'List',
            component: List
        },
        {
            path: "*",
            component: PageNotFound
        }
    ]
})