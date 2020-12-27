import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {SearchComponent} from "./compound-search/compound-search.component";
import {ViewComponent} from "./compound-view/compound-view.component";
import {CreateComponent} from "./compound-new/compound-new.component";
import {CompoundEditComponent} from "./compound-edit/compound-edit.component";

const routes: Routes = [
  {
    path: '',
    data: {
      breadcrumb: 'Compounds'
    },
    children: [
      {
        path: '',
        data: {
          breadcrumb: null
        },
        component: SearchComponent
      },
      {
        path: 'new',
        data: {
          breadcrumb: 'Create'
        },
        component: CreateComponent
      },
      {
        path: ':id',
        data: {
          breadcrumb: null
        },
        children: [
          {
            path: '',
            data: {
              breadcrumb: 'Compound'
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
                path: 'edit',
                data: {
                  breadcrumb: 'Edit'
                },
                component: CompoundEditComponent
              }
            ]
          }
        ]
      }
    ]
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class CompoundsRoutingModule {
}
