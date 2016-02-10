/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lop.api;

import com.lop.model.Link;
import com.lop.model.User;
import com.lop.model.Users;
import com.lop.model.World;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.POST;
import javax.ws.rs.PathParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.jboss.weld.context.http.HttpRequestContext;

/**
 * REST Web Service
 *
 * @author Anh
 */
@Path("/users")
@Consumes(MediaType.APPLICATION_XML)
@Produces(MediaType.APPLICATION_XML)
public class UsersResource {

    @Context
    private UriInfo uriInfo;

    /**
     * Creates a new instance of UsersResource
     */
    public UsersResource() {
    }

    public UsersResource(@Context UriInfo uriInfo) {
        this.uriInfo = uriInfo;
    }

    /**
     * Retrieves representation of an instance of com.lop.api.UsersResource
     *
     * @return an instance of com.lop.model.Users
     */
    @GET
    public List<User> getXml(@PathParam("boardId") String boardId) {
        List<User> users;
        if (boardId != null) {
            users = new ArrayList<>(World.getInstance().getBoards().getById().get(boardId).getUsers());
        } else {
            users = new ArrayList<>(World.getInstance().getUsers().getById().values());
        }
        for (User u : users) {
            Link.addLinks(u, uriInfo);
        }
        return users;
    }

    /**
     * POST method for creating an instance of UserResource
     *
     * @param content representation for the new resource
     * @return an HTTP response with content of the created resource
     */
    @POST
    public Response postXml(@PathParam("boardId") String boardId, User content) {
        if (boardId != null) {
            System.out.println("lala:" + content.getId());
            content = World.getInstance().getUsers().getById().get(Integer.toString(content.getId()));
            World.getInstance().getBoards().getById().get(boardId).addUser(content);
            System.out.println(uriInfo.toString());
            System.out.println(content.getId());
            URI uri = uriInfo.getAbsolutePathBuilder().path(Integer.toString(content.getId())).build();
            return Response.created(uri)
                    .entity(content)
                    .build();
        } else {
            World.getInstance().getUsers().add(content);
            URI uri = uriInfo.getAbsolutePathBuilder().path(Integer.toString(content.getId())).build();
            return Response.created(uri)
                    .entity(content)
                    .build();
        }
    }

    /**
     * Sub-resource locator method for {id}
     */
    @GET
    @Path("{id}")
    public Response getUserResource(@PathParam("id") String id) {
        User user = Link.addLinks(World.getInstance().getUsers().get(id), uriInfo);
        return Response.ok(Link.getUriForSelf(user, uriInfo))
                .entity(user)
                .build();
    }

    @POST
    @Path("login")
    public Response login(@Context HttpServletRequest request, User u) {
        User me = World.getInstance().getUsers().login(u);
        if (me != null) {
            HttpSession session = request.getSession();
            me = Link.addLinks(me, uriInfo);
            session.setAttribute("me", me);
            // User userNoPassword = new User(me);
            URI uri = uriInfo.getAbsolutePathBuilder()
                    .path(UsersResource.class)
                    .path(Integer.toString(me.getId()))
                    .build();
            return Response.ok(uri)
                    .entity(me)
                    .build();
        } else {
            return Response.status(400)
                    .build();
        }
    }
}