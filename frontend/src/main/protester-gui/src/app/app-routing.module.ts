import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';

const routes: Routes = [
  {
    path: '',
    children: [
      {
        path: '',
        redirectTo: 'account',
        pathMatch: 'full'
      },
      {
        path: 'account',
        loadChildren: 'src/app/shared/shared.module#SharedModule'
      },
      {
        path: 'projects-menu',
        loadChildren: 'src/app/projects-menu/projects-menu.module#ProjectsMenuModule'
      },
      {
        path: 'libraries-menu',
        loadChildren: 'src/app/libraries-menu/libraries-menu.module#LibrariesMenuModule'
      },
      {
        path: '**',
        redirectTo: '404'
      }
    ]
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes, { useHash: true })],
  exports: [RouterModule]
})
export class AppRoutingModule {
}
