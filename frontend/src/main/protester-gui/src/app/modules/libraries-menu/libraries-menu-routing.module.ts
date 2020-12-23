import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {ViewComponent} from "./view/view.component";

const routes: Routes = [
  {
    path: '',
    data: {
      breadcrumb: 'Libraries menu'
    },
    children: [
      {
        path: '',
        data: {
          breadcrumb: null
        },
        component: ViewComponent
      },
      {
        path: 'libraries',
        data: {
          breadcrumb: null
        },
        loadChildren: 'src/app/modules/libraries-menu/libraries/libraries.module#LibrariesModule'
      },
      {
        path: 'compounds',
        data: {
          breadcrumb: null
        },
        loadChildren: 'src/app/modules/libraries-menu/compounds/compounds.module#CompoundsModule'
      },
      {
        path: 'actions',
        data: {
          breadcrumb: null
        },
        loadChildren: 'src/app/modules/libraries-menu/actions/actions.module#ActionsModule'
      }
    ]
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class LibrariesMenuRoutingModule { }
